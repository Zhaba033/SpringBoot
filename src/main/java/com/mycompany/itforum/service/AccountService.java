package com.mycompany.itforum.service;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountService {
    
    PasswordEncoder passwordEncoder;
    AccountRepository accountRepository;
    
    public void createAccount(Account a) {
        a.setPassword(passwordEncoder.encode(a.getPassword()));
        accountRepository.save(a);
    }
    
    public void changePassword(Account a, String newPassword) {
        a.setPassword(passwordEncoder.encode(a.getPassword()));
        accountRepository.save(a);
    }
    
}
