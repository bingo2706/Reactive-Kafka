package com.tanthanh.accountservice.event;

import com.google.gson.Gson;
import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.service.iml.IAccountService;
import com.tanthanh.accountservice.utils.Constant;
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
    IAccountService accountService;

    @Autowired
    private KafkaSender<String, String> sender;

    public EventConsumer(ReceiverOptions<String, String> ReceiverOptions){
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.CHECK_BALANCE_TOPIC)))
                .receive().subscribe(this::printText);
    }
    public void printText(ReceiverRecord <String,String> receiverRecord){

            AccountDTO dto = new AccountDTO();
            dto = gson.fromJson(receiverRecord.value(),AccountDTO.class);


        log.info(receiverRecord.value());

        AccountDTO finalDto = dto;
        accountService.checkBalance(dto.getBalance(),dto.getEmail()).subscribe(aBoolean ->  {
            log.info("Check balance of account "+ finalDto.getEmail()+" is "+aBoolean);
            if (aBoolean)  finalDto.setCheckBalance(true);
            else finalDto.setCheckBalance(false);

            sender
                    .send(Mono.just(SenderRecord.create(new ProducerRecord<>(Constant.RES_BALANCE_TOPIC,gson.toJson(finalDto)),gson.toJson(finalDto))))
                    .then()
                    .doOnError(e -> log.error("Send failed", e))
                    .doOnSuccess(sender -> log.info("Success ! "+sender))
                    .subscribe();
        });
    }
}
