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
    public Mono<AccountDTO> subtract(double amount, long id) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    account.setReserved(account.getReserved()-amount);
                    account.setBalance(account.getBalance()-amount);
                    return accountRepository.save(account);
                })
                .map(AccountDTO::entityToModel)
                .switchIfEmpty(Mono.error(new AccountException("Account not found")));
    }

    @Override
    public Mono<Boolean> reserved(double reserved, long id) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    if(reserved+ account.getReserved() <= account.getBalance()){
                        account.setReserved(account.getReserved()+reserved);
                        accountRepository.save(account).subscribe();
                        return Mono.just(true);
                    }else return Mono.just(false);
                })
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
                .delayElements(Duration.ofSeconds(1))
                .doFirst(() -> log.info("Retrieving all account"))
                .doOnNext(account -> log.info("Account found: {}", account))
                .doOnError(ex -> log.warn("Something went wrong while retrieving the accounts", ex));

    }

}
