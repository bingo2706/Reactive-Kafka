package com.tanthanh.accountservice.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.service.iml.exception.AccountException;
import com.tanthanh.accountservice.repository.AccountRepository;
import com.tanthanh.accountservice.service.iml.IAccountService;
import lombok.extern.slf4j.Slf4j;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;


import java.time.Duration;



@Slf4j
@Service
public class AccountService implements IAccountService {
    @Autowired
    AccountRepository accountRepository;
    Gson gson = new Gson();
    @Autowired
    private  KafkaSender<String, String> sender;

    @Value("${kafka.topic}")
    String topicName;
    @Override
    public Mono<AccountDTO> subtract(AccountDTO model) {
        return accountRepository.findByEmail(model.getEmail())
                .flatMap(account -> {
                    account.setReserved(account.getReserved()-model.getBalance());
                    account.setBalance(account.getBalance()-model.getBalance());
                    return accountRepository.save(account);
                })


                .map(AccountDTO::fromModel)

                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }

    @Override
    public Mono<AccountDTO> reserved(double reserved, String email) {
        return accountRepository.findByEmail(email)
                .flatMap(account -> {
                    if(reserved+ account.getReserved() <= account.getBalance()){
                        account.setReserved(account.getReserved()+reserved);

                    }else{
                        Mono.error(new AccountException("reserved can't greater than balance"));
                    }
                    return accountRepository.save(account);
                })
                .map(AccountDTO::fromModel)
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));


    }

    @Override
    public Mono<Boolean> checkBalance(double balance,String email) {
        return accountRepository.findByEmail(email)
                .map(account -> {
                    if(balance <= account.getBalance() - account.getReserved())
                        return true;
                    else return false;
                });
    }

    @Override
    public Flux<AccountDTO> getAllAccount() {
        return accountRepository.findAll()
                .map(AccountDTO::fromModel)
                .delayElements(Duration.ofSeconds(1))
                .doFirst(() -> log.info("Retrieving all account"))
                .doOnNext(account -> log.info("Account found: {}", account))
                .doOnError(ex -> log.warn("Something went wrong while retrieving the accounts", ex));

    }

}
