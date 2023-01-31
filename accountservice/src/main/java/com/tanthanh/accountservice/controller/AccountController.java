package com.tanthanh.accountservice.controller;

import com.tanthanh.accountservice.model.AccountDTO;
import com.tanthanh.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping()
    public Flux<AccountDTO> getAllAccount(){

        return accountService.getAllAccount();
    }
    @GetMapping("/checkBalance/{id}")
    public Mono<AccountDTO> checkBalance(@PathVariable long id){
        return accountService.checkBalance(id);
    }


}
