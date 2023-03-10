package com.tanthanh.paymentservice.controller;

import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.dto.PaymentDTO;
import com.tanthanh.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping("/payment")
    public Mono<ResponseEntity<PaymentDTO>> payment(@RequestBody PaymentDTO paymentDTO){
        return paymentService.payment(paymentDTO).map(ResponseEntity::ok);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Flux<PaymentDTO>> payment(@PathVariable int id){
        return ResponseEntity.ok(paymentService.getAllPayment(id));
    }

}
