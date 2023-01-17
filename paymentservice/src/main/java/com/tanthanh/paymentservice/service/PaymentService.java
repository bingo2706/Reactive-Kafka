package com.tanthanh.paymentservice.service;

import com.google.gson.Gson;
import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
public class PaymentService {
    Gson gson = new Gson();
    @Autowired
    private KafkaSender<String, String> sender;



    public Mono<AccountDTO> payment(AccountDTO accountDTO){

        sender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<>(Constant.CHECK_BALANCE_TOPIC,gson.toJson(accountDTO)),gson.toJson(accountDTO))))
                .then()
                .doOnError(e -> log.error("Send failed", e))
                .doOnSuccess(sender -> log.info("Success ! "+sender))
                .subscribe();

         return Mono.empty();
    }
}
