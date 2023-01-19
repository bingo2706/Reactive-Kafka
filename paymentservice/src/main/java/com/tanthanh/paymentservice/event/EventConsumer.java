package com.tanthanh.paymentservice.event;

import com.google.gson.Gson;
import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.dto.PaymentDTO;
import com.tanthanh.paymentservice.service.PaymentService;
import com.tanthanh.paymentservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.Collections;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    @Autowired
    private KafkaSender<String, String> sender;

    @Autowired
    private PaymentService paymentService;

    public EventConsumer(ReceiverOptions<String, String> ReceiverOptions){
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.PAYMENT_COMPLETED_TOPIC)))
                .receive().subscribe(this::paymentComplete);
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.PAYMENT_CREATED_TOPIC)))
                .receive().subscribe(this::paymentCreated);
    }
    public void paymentComplete(ReceiverRecord <String,String> receiverRecord){
        log.info("Payment complete event "+receiverRecord.value());
        PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(),PaymentDTO.class);
        paymentService.updateStatusPayment(paymentDTO).subscribe(result -> log.info("End process payment "+result));
    }
    public void paymentCreated(ReceiverRecord <String,String> receiverRecord){
        log.info("Payment Created event "+receiverRecord.value());
        PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(),PaymentDTO.class);
        paymentService.updateStatusPayment(paymentDTO).subscribe(result -> log.info("Update Status  "+result));
    }

}

