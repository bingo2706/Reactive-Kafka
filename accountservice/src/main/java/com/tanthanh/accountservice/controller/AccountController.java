package com.tanthanh.accountservice.controller;

import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.service.iml.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    IAccountService accountService;

    @GetMapping()
    public Flux<AccountDTO> GetAllAccount(){

        return accountService.getAllAccount();
    }
    @GetMapping("/checkBalance/{id}")
    public Mono<AccountDTO> checkBalance(@PathVariable long id){
        return accountService.checkBalance(id);
    }


}
