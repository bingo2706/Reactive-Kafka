package com.tanthanh.paymentservice.repository;

import com.tanthanh.paymentservice.data.Payment;
import com.tanthanh.paymentservice.dto.PaymentDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PaymentRepository extends ReactiveCrudRepository<Payment,Integer> {
    @Query("SELECT * FROM payment WHERE account_id = :id")
    Flux<Payment> findByAccountId(int id);
}
