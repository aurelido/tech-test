package com.aabanegas.techtest.repository;

import com.aabanegas.techtest.domain.Payment;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends CassandraRepository<Payment, UUID> {

    Iterable<Payment> findAll(Sort sort);

    Page<Payment> findAll(Pageable pageable);
}