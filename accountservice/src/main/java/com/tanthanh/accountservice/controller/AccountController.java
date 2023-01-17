package com.tanthanh.accountservice.controller;

import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.repository.AccountRepository;
import com.tanthanh.accountservice.service.iml.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.beans.BeanProperty;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    IAccountService accountService;

    @GetMapping()
    public Flux<AccountDTO> GetAllAccount(){

        return accountService.getAllAccount();
    }
    @PostMapping("/checkBalance")
    public Mono<Boolean> checkBalance(@RequestBody AccountDTO model){
        return accountService.checkBalance(model.getBalance(),model.getEmail());
    }
    @PutMapping("/reserved")
    public Mono<AccountDTO> reserved(@RequestBody AccountDTO model){
        return accountService.reserved(model.getReserved(),model.getEmail());
    }
    @PutMapping("/subtract")
    public Mono<AccountDTO> subtract(@RequestBody AccountDTO model) throws Exception {
        return accountService.subtract(model);
    }
}
