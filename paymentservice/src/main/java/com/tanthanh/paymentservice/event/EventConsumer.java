package com.tanthanh.paymentservice.event;

import com.google.gson.Gson;
import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    public EventConsumer(ReceiverOptions<String, String> ReceiverOptions){
        KafkaReceiver.create(ReceiverOptions.subscription(Collections.singleton(Constant.RES_BALANCE_TOPIC)))
                .receive().subscribe(this::printText);
    }
    public void printText(ReceiverRecord<String,String> receiverRecord){
        AccountDTO dto = new AccountDTO();
        dto = gson.fromJson(receiverRecord.value(),AccountDTO.class);
        log.info(dto.toString());
    }
}

