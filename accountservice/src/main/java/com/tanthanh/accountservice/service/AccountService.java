package com.tanthanh.accountservice.service;
import com.tanthanh.accountservice.model.AccountDTO;

import com.tanthanh.accountservice.repository.AccountRepository;


import com.tanthanh.commonservice.common.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountService{
    @Autowired
    AccountRepository accountRepository;


    public Mono<Integer> rollBackReserved(double reserved, long id) {
        return findById(id)
                .flatMap(account -> {
                        account.setReserved(account.getReserved()-reserved);
                        return accountRepository.rollbackReserved(account.getId(),account.getReserved()).last();
                });

    }


    public Mono<Integer> subtract(double amount, long id) {
        return findById(id)
                .flatMap(account -> {
                    account.setReserved(account.getReserved()-amount);
                    account.setBalance(account.getBalance()-amount);
                    return accountRepository.subtract(account.getId(),account.getReserved(),account.getBalance()).last();
                });

    }


    public Mono<Integer> reserved(double reserved, long id) {
        return findById(id)
                .flatMap(account -> accountRepository.reserved(account.getId(),reserved).last());

    }

    public Mono<AccountDTO> checkBalance(long id) {
        return findById(id);

    }
    public Mono<AccountDTO> findById(long id){
        return accountRepository.findById(id)
                .map(AccountDTO::entityToModel)
               .switchIfEmpty(Mono.error(new CommonException("A01", "Account not found", HttpStatus.NOT_FOUND)));

    }

    public Flux<AccountDTO> getAllAccount() {
        return accountRepository.findAll()
                .map(AccountDTO::entityToModel)
                .doOnError(ex -> log.warn("Something went wrong while retrieving the accounts", ex));

    }

}
