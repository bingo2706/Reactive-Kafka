package com.tanthanh.accountservice.service;
import com.google.gson.Gson;
import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.exception.AccountException;
import com.tanthanh.accountservice.repository.AccountRepository;
import com.tanthanh.accountservice.service.iml.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import java.time.Duration;
@Slf4j
@Service
public class AccountService implements IAccountService {
    @Autowired
    AccountRepository accountRepository;
    Gson gson = new Gson();
    @Autowired
    private  KafkaSender<String, String> sender;

    @Override
    public Mono<Integer> rollBackReserved(double reserved, long id) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                        account.setReserved(account.getReserved()-reserved);
                        return accountRepository.rollbackReserved(account.getId(),account.getReserved()).last();
                })
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }

    @Override
    public Mono<Integer> subtract(double amount, long id) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    account.setReserved(account.getReserved()-amount);
                    account.setBalance(account.getBalance()-amount);
                    return accountRepository.subtract(account.getId(),account.getReserved(),account.getBalance()).last();
                })
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }

    @Override
    public Mono<Integer> reserved(double reserved, long id) {
        return accountRepository.findById(id)
                .flatMap(account -> accountRepository.reserved(account.getId(),reserved).last())
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }
    @Override
    public Mono<AccountDTO> checkBalance(long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::entityToModel)
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }

    @Override
    public Flux<AccountDTO> getAllAccount() {
        return accountRepository.findAll()
                .map(AccountDTO::entityToModel)
                .doOnError(ex -> log.warn("Something went wrong while retrieving the accounts", ex));

    }

}
