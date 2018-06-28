package com.aabanegas.techtest.repository;

import com.aabanegas.techtest.domain.Payment;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the Payment entity.
 */
@Repository
public class PaymentRepositoryImpl {

	public static final String PAYMENT_BY_CLIENT_REF_CACHE = "paymentsByClientRef";

	private final Session session;

	private final Validator validator;

	private Mapper<Payment> mapper;

	private PreparedStatement findAllStmt;

	private PreparedStatement findByClientRefStmt;

	private PreparedStatement insertByClientRefStmt;

	private PreparedStatement deleteByClientRefStmt;

	private PreparedStatement insertByCardNumberStmt;

	private PreparedStatement deleteByCardNumberStmt;

	private PreparedStatement insertByAmountStmt;

	private PreparedStatement deleteByAmountStmt;

	private PreparedStatement insertByTaxAmountStmt;

	private PreparedStatement deleteByTaxAmountStmt;

	private PreparedStatement insertByCurrencyStmt;

    private PreparedStatement insertByExecTimeStmt;

	private PreparedStatement deleteByCurrencyStmt;

	public PaymentRepositoryImpl(Session session, Validator validator) {
		this.session = session;
		this.validator = validator;
		mapper = new MappingManager(session).mapper(Payment.class);

		findAllStmt = session.prepare("SELECT * FROM payment");

		findByClientRefStmt = session
				.prepare("SELECT id " + "FROM payment_by_client_ref " + "WHERE client_ref = :client_ref");

		insertByClientRefStmt = session
				.prepare("INSERT INTO payment_by_client_ref (client_ref, id) " + "VALUES (:client_ref, :id)");

		deleteByClientRefStmt = session.prepare(
	            "DELETE FROM payment_by_client_ref WHERE client_ref = :client_ref");

		insertByCardNumberStmt = session
				.prepare("INSERT INTO payment_by_card_number (card_number, id) " + "VALUES (:card_number, :id)");

		deleteByCardNumberStmt = session.prepare(
	            "DELETE FROM payment_by_card_number WHERE card_number = :card_number");

		insertByAmountStmt = session
				.prepare("INSERT INTO payment_by_amount (amount, id) " + "VALUES (:amount, :id)");

		deleteByAmountStmt = session.prepare(
	            "DELETE FROM payment_by_amount WHERE amount = :amount");

		insertByTaxAmountStmt = session
				.prepare("INSERT INTO payment_by_tax_amount (tax_amount, id) " + "VALUES (:tax_amount, :id)");

		deleteByTaxAmountStmt = session.prepare(
	            "DELETE FROM payment_by_tax_amount WHERE tax_amount = :tax_amount");

		insertByCurrencyStmt = session
				.prepare("INSERT INTO payment_by_currency (currency, id) " + "VALUES (:currency, :id)");

        insertByExecTimeStmt = session
                .prepare("INSERT INTO payment_by_exec_time (exec_time, id) " + "VALUES (:exec_time, :id)");

		deleteByCurrencyStmt = session.prepare(
	            "DELETE FROM payment_by_currency WHERE currency = :currency");

	}

	public Optional<Payment> findById(UUID uuid) {
		return Optional.ofNullable(mapper.get(uuid));
	}

	@Cacheable(cacheNames = PAYMENT_BY_CLIENT_REF_CACHE)
	public List<Payment> findByClientRef(String clientRef) {
		BoundStatement stmt = findByClientRefStmt.bind();
		stmt.setString("clientRef", clientRef);
		return mapper.map(session.execute(stmt)).all();
	}

	public List<Payment> findAll() {
		return mapper.map(session.execute(findAllStmt.bind())).all();
	}

	public Payment save(Payment payment) {
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Payment oldPayment = mapper.get(payment.getUuid());
        if (oldPayment != null) {
	        	if (!StringUtils.isEmpty(oldPayment.getClientRef()) && !oldPayment.getClientRef().equals(payment.getClientRef())) {
	        		session.execute(deleteByClientRefStmt.bind().setInt("client_ref", oldPayment.getClientRef()));
	        	}
	        	if (!StringUtils.isEmpty(oldPayment.getCardNumber()) && oldPayment.getCardNumber() != payment.getCardNumber()) {
	        		session.execute(deleteByCardNumberStmt.bind().setLong("card_number", oldPayment.getCardNumber()));
	        	}
	        	if (!StringUtils.isEmpty(oldPayment.getAmount()) && !oldPayment.getAmount().equals(payment.getAmount())) {
	        		session.execute(deleteByAmountStmt.bind().setDecimal("amount", oldPayment.getAmount()));
	        	}
	        	if (!StringUtils.isEmpty(oldPayment.getTaxAmount()) && oldPayment.getTaxAmount() != payment.getTaxAmount()) {
	        		session.execute(deleteByTaxAmountStmt.bind().setFloat("tax_amount", oldPayment.getTaxAmount()));
	        	}
	        	if (!StringUtils.isEmpty(oldPayment.getCurrencyAlphaCode()) &&
	        			!oldPayment.getCurrencyAlphaCode().equals(payment.getCurrencyAlphaCode())) {
	        		String currencyCode = oldPayment.getCurrencyAlphaCode();
	        		session.execute(deleteByCurrencyStmt.bind().setString("currency", currencyCode));
	        	}
        }

        BatchStatement batch = new BatchStatement();
        batch.add(mapper.saveQuery(payment));

        batch.add(insertByClientRefStmt.bind()
            .setInt("client_ref", payment.getClientRef())
            .setUUID("id", payment.getUuid()));

        batch.add(insertByCardNumberStmt.bind()
            .setLong("card_number", payment.getCardNumber())
            .setUUID("id", payment.getUuid()));

        batch.add(insertByAmountStmt.bind()
            .setDecimal("amount", payment.getAmount())
            .setUUID("id", payment.getUuid()));

        batch.add(insertByTaxAmountStmt.bind()
            .setFloat("tax_amount", payment.getTaxAmount())
            .setUUID("id", payment.getUuid()));

        batch.add(insertByCurrencyStmt.bind()
            .setString("currency", payment.getCurrencyAlphaCode())
            .setUUID("id", payment.getUuid()));

        batch.add(insertByExecTimeStmt.bind()
            .setString("exec_time", LocalDate.now().toString())
            .setUUID("id", payment.getUuid()));

        session.execute(batch);
        return payment;
    }

}
