package com.tanthanh.accountservice.event;

import com.google.gson.Gson;
import com.tanthanh.accountservice.dto.PaymentDTO;
import com.tanthanh.accountservice.service.iml.IAccountService;
import com.tanthanh.accountservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;

import java.util.Collections;
import java.util.Objects;

@Service
@Slf4j
public class EventConsumer {

    Gson gson = new Gson();

    @Autowired
    IAccountService accountService;

    @Autowired
    private KafkaSender<String, String> sender;

    @Autowired
    private EventProducer eventProducer;
    public EventConsumer(ReceiverOptions<String, String> ReceiverOptions){

        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.PAYMENT_REQUEST_TOPIC)))
                .receive().subscribe(this::paymentRequest);
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.PAYMENT_COMPLETED_TOPIC)))
                .receive().subscribe(this::paymentComplete);
    }

    public void paymentRequest(ReceiverRecord <String,String> receiverRecord){
        PaymentDTO dto = gson.fromJson(receiverRecord.value(),PaymentDTO.class);
        accountService.reserved(dto.getAmount(),dto.getAccountId())
                .subscribe(isReversedSuccessful -> {
                    if(isReversedSuccessful == 0){
                        dto.setStatus(Constant.STATUS_PAYMENT_REJECTED);
                        dto.setReserved(false);
                        eventProducer.sendPaymentComplete(Constant.PAYMENT_COMPLETED_TOPIC,gson.toJson(dto));
                    }else {
                        dto.setStatus(Constant.STATUS_PAYMENT_PROCESSING);
                        dto.setReserved(true);
                        eventProducer.sendPaymentCreated(Constant.PAYMENT_CREATED_TOPIC,gson.toJson(dto));
                    }
                });
    }
    public void paymentComplete(ReceiverRecord <String,String> receiverRecord){
        log.info("Payment Complete event");
        PaymentDTO dto = gson.fromJson(receiverRecord.value(),PaymentDTO.class);
        if(Objects.equals(dto.getStatus(), Constant.STATUS_PAYMENT_SUCCESSFUL)){
            accountService.subtract(dto.getAmount(),dto.getAccountId()).subscribe(isSuccessful -> {
                if(isSuccessful == 1) log.info("Subtract successfully");
                else log.info("Subtract failed");
            } );
        }else{
            if(dto.isReserved()){
                accountService.rollBackReserved(dto.getAmount(),dto.getAccountId()).subscribe(isSuccessful -> {
                    if(isSuccessful == 1) log.info("Rollback reserved successfully");
                    else log.info("Rollback reserved failed");
                } );
            }else{
                log.info("Can't reserved");
            }

        }

    }
   
}
