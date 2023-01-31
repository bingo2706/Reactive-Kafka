package com.tanthanh.accountservice.service;


import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.model.AccountDTO;
import com.tanthanh.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    private AccountDTO accountDTO;
    private Account account;
    private long id;
    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(accountService, "accountRepository", accountRepository);
        id = 4;
        accountDTO = new AccountDTO(id,"test1@gmail.com","USD",2000,0);
        account = new Account(id,"test1@gmail.com","USD",2000,0);
    }
    @Test
    void getAllAccount(){
        when(accountRepository.findAll()).thenReturn(Flux.just(account));
        accountService.getAllAccount().doOnNext(Assertions::assertNotNull).subscribe();

        when(accountRepository.findAll()).thenReturn(Flux.error(new Exception("Some Error")));
        accountService.getAllAccount().doOnError(Assertions::assertNotNull).subscribe();
    }
    @Test
    void findById(){
        when(accountRepository.findById(id)).thenReturn(Mono.just(account));
        accountService.findById(id).doOnNext(Assertions::assertNotNull).subscribe();

        when(accountRepository.findById(id)).thenReturn(Mono.empty());
        accountService.findById(id).doOnError( ret -> assertEquals("Account not found",ret.getMessage())).subscribe();
    }
    @Test
    void checkBalance(){
        when(accountRepository.findById(id)).thenReturn(Mono.just(account));
        accountService.checkBalance(id).doOnNext(Assertions::assertNotNull).subscribe();
    }
    @Test
    void reserved(){
        when(accountRepository.findById(id)).thenReturn(Mono.just(account));
        when(accountRepository.reserved(id,200)).thenReturn(Flux.just(1));
        accountService.reserved(anyDouble(),id).doOnNext(ret -> assertEquals(1,ret)).subscribe();
    }
    @Test
    void subtract(){
        when(accountRepository.findById(id)).thenReturn(Mono.just(account));
        when(accountRepository.subtract(account.getId(),200,200)).thenReturn(Flux.just(1));
        accountService.subtract(anyDouble(),id).doOnNext(ret -> assertEquals(1,ret)).subscribe();
    }
    @Test
    void rollBackReserved(){
        when(accountRepository.findById(id)).thenReturn(Mono.just(account));
        when(accountRepository.rollbackReserved(account.getId(),200)).thenReturn(Flux.just(1));
        accountService.rollBackReserved(anyDouble(),id).doOnNext(ret -> assertEquals(1,ret)).subscribe();
    }
}
