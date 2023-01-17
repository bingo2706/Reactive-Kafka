package com.tanthanh.paymentservice.controller;

import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping("/payment")
    public Mono<AccountDTO> payment(@RequestBody AccountDTO accountDTO){
        return paymentService.payment(accountDTO);
    }

}
