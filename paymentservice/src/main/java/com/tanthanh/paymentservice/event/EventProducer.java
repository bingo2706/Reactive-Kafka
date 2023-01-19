package com.tanthanh.paymentservice.event;

import com.google.gson.Gson;
import com.tanthanh.paymentservice.utils.Constant;
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

    public void sendPaymentRequest(String topic,String message){
        sender
            .send(Mono.just(SenderRecord.create(new ProducerRecord<>(topic,message),message)))
            .then()
            .doOnError(e -> log.error("Send event Payment Request Failed", e))
            .doOnSuccess(sender -> log.info("Send event Payment Request Success ! "))
            .subscribe();
         }

}
