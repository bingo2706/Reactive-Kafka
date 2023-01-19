package com.tanthanh.paymentprocessingservice.event;

import com.google.gson.Gson;
import com.tanthanh.paymentprocessingservice.dto.PaymentDTO;
import com.tanthanh.paymentprocessingservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;

import java.util.Collections;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    @Autowired
    private KafkaSender<String, String> sender;

    @Autowired
    private EventProducer eventProducer;

    public EventConsumer(ReceiverOptions<String, String> ReceiverOptions){
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.PAYMENT_CREATED_TOPIC)))
                .receive().subscribe(this::paymentCreated);

    }
    public void paymentCreated(ReceiverRecord<String,String> receiverRecord){
        try {
            log.info("Processing payment ...");
            PaymentDTO dto = gson.fromJson(receiverRecord.value(),PaymentDTO.class);
            dto.setStatus(Constant.STATUS_PAYMENT_SUCCESSFUL);
            Thread.sleep(3000);
            eventProducer.sendPaymentComplete(Constant.PAYMENT_COMPLETED_TOPIC,gson.toJson(dto));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
