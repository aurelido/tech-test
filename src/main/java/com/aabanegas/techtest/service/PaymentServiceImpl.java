package com.aabanegas.techtest.service;

import com.aabanegas.techtest.domain.Payment;
import com.aabanegas.techtest.repository.PaymentRepositoryImpl;
import com.aabanegas.techtest.service.dto.PaymentDTO;
import com.aabanegas.techtest.service.dto.TransactionDTO;
import com.aabanegas.techtest.service.mapper.MapperUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing Payments
 */
@CommonsLog
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepositoryImpl paymentRepository;

    @Autowired
    private MapperUtils mapperUtils;

    /**
     * Save a payment.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UUID save(TransactionDTO transactionDTO) {
        log.debug(String.format("Request to save Payment : %s", transactionDTO));
        Payment payment = mapperUtils.transactionDtoToPayment(transactionDTO);
        payment = paymentRepository.save(payment);
        return payment.getUuid();
    }

    @Override
    public List<PaymentDTO> findByClientRef(String clientRef) {
        return paymentRepository.findByClientRef(clientRef).stream()
                .map(mapperUtils::paymentToPaymentDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities
     */
    @Override
    public List<PaymentDTO> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll().stream()
            .map(mapperUtils::paymentToPaymentDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<PaymentDTO> findOne(UUID id) {
        log.debug(String.format("Request to get Payment : %s", id));
        return paymentRepository.findById(id)
            .map(mapperUtils::paymentToPaymentDto);
    }

}
