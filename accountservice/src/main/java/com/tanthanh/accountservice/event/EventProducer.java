package com.tanthanh.accountservice.event;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
public class EventProducer {
    @Autowired
    private KafkaSender<String, String> sender;

    public void sendPaymentComplete(String topic,String message){
        sender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<>(topic,message),message)))
                .then()
                .doOnError(e -> log.error("Send event Payment Complete Failed", e))
                .doOnSuccess(sender -> log.info("Send event Payment Complete Success ! "))
                .subscribe();
    }
    public void sendPaymentCreated(String topic,String message){
        sender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<>(topic,message),message)))
                .then()
                .doOnError(e -> log.error("Send event Payment Created Failed", e))
                .doOnSuccess(sender -> log.info("Send event Payment Created Success ! "))
                .subscribe();
    }
}
