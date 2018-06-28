package com.aabanegas.techtest.configuration;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.ClusterBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.util.StringUtils;

import com.codahale.metrics.MetricRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TupleType;
import com.datastax.driver.core.TupleValue;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;
import com.datastax.driver.extras.codecs.jdk8.ZonedDateTimeCodec;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Configuration
@EnableCassandraRepositories(
        basePackages = "com.aabanegas.techtest.repository")
@Profile({"dev", "prod"})
public class CassandraConfiguration {

	@Value("${spring.data.cassandra.protocolVersion:V4}")
    private ProtocolVersion protocolVersion;

    @Autowired(required = false)
    MetricRegistry metricRegistry;

    @Autowired
    private Cluster cluster;

    @PostConstruct
    public void postConstruct() {
        TupleType tupleType = cluster.getMetadata()
            .newTupleType(DataType.timestamp(), DataType.varchar());

        cluster.getConfiguration().getCodecRegistry()
            .register(LocalDateCodec.instance)
            .register(InstantCodec.instance)
            .register(new ZonedDateTimeCodec(tupleType));

        if (metricRegistry != null) {
            cluster.init();
            metricRegistry.registerAll(cluster.getMetrics().getRegistry());
        }
    }

    @Bean
    public CassandraCustomConversions cassandraCustomConversions(Cluster cluster) {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(TupleToZonedDateTimeConverter.INSTANCE);
        converters.add(new ZonedDateTimeToTupleConverter(protocolVersion, cluster.getConfiguration().getCodecRegistry()));
        return new CassandraCustomConversions(converters);
    }

    @ReadingConverter
    enum TupleToZonedDateTimeConverter implements Converter<TupleValue, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(TupleValue source) {
            java.util.Date timestamp = source.getTimestamp(0);
            ZoneId zoneId = ZoneId.of(source.getString(1));
            return timestamp.toInstant().atZone(zoneId);
        }
    }

    @WritingConverter
    class ZonedDateTimeToTupleConverter implements Converter<ZonedDateTime, TupleValue> {

        private TupleType type;

        public ZonedDateTimeToTupleConverter(ProtocolVersion version, CodecRegistry codecRegistry) {
            type = TupleType.of(version, codecRegistry, DataType.timestamp(), DataType.text());
        }

        @Override
        public TupleValue convert(ZonedDateTime source) {
            TupleValue tupleValue = type.newValue();
            tupleValue.setTimestamp(0, Date.from(source.toLocalDateTime().toInstant(ZoneOffset.UTC)));
            tupleValue.setString(1, source.getZone().toString());
            return tupleValue;
        }
    }

    @Bean
    ClusterBuilderCustomizer clusterBuilderCustomizer(CassandraProperties properties) {
        return builder -> builder
            .withProtocolVersion(protocolVersion)
            .withPort(getPort(properties));
    }

    protected int getPort(CassandraProperties properties) {
        return properties.getPort();
    }

    @Bean(destroyMethod = "close")
    public Session session(CassandraProperties properties, Cluster cluster) {
        log.debug("Configuring Cassandra session");
        return StringUtils.hasText(properties.getKeyspaceName()) ? cluster.connect(properties.getKeyspaceName()) : cluster.connect();
    }
}
