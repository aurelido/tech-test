package com.aabanegas.techtest.service;

import com.aabanegas.techtest.service.dto.PaymentDTO;
import com.aabanegas.techtest.service.dto.TransactionDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Payment.
 */
public interface PaymentService {

    /**
     * Save a payment.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    UUID save(TransactionDTO transactionDTO);

    /**
     * Get all "clientRef" payment.
     *
     * @param clientRef the clientRef of the entity
     * @return the entity
     */
    List<PaymentDTO> findByClientRef(String clientRef);

    /**
     * Get all the payments.
     *
     * @return the list of entities
     */
    List<PaymentDTO> findAll();


    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentDTO> findOne(UUID id);

}
