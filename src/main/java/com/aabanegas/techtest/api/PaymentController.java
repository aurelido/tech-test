package com.aabanegas.techtest.api;

import com.aabanegas.techtest.domain.Payment;
import com.aabanegas.techtest.service.PaymentServiceImpl;
import com.aabanegas.techtest.service.dto.AmountDTO;
import com.aabanegas.techtest.service.dto.TransactionDTO;
import com.aabanegas.techtest.service.dto.PaymentDTO;
import com.aabanegas.techtest.service.mapper.CurrencyMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CommonsLog
@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    CurrencyMapper mapper;


    @GetMapping("/async/payments/{clientRef}")
    public CompletableFuture<Payment> findAsyncPayment(@PathVariable(value = "clientRef") String clientRef)
            throws InterruptedException, ExecutionException {
        return null;
    }

    @GetMapping("/payments/{clientRef}")
    public ResponseEntity<List<PaymentDTO>> findPayment(@PathVariable("clientRef") String clientRef) {
        List<PaymentDTO> payments = paymentService.findByClientRef(clientRef);
        return payments.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(payments);

    }

    @GetMapping("/payments/{uuid}")
    public ResponseEntity<PaymentDTO> findOnePayment(@PathVariable("uuid") UUID uuid) {
        return paymentService.findOne(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug(String.format("REST request to save Payment : %s", transactionDTO));

        UUID uuid = paymentService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/payments/" + uuid)).body(transactionDTO);
    }

}
