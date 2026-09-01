package com.mycompany.itforum.service;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountService {

    final PasswordEncoder passwordEncoder;
    final AccountRepository accountRepository;

    public void createAccount(Account a) {
        a.setPassword(passwordEncoder.encode(a.getPassword()));
        a.getRoles().add("USER");
        accountRepository.save(a);
    }

    public void changePassword(Account a, String newPassword) {
        a.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(a);
    }

    public void ban(Account a) { // give BANNED
        if (a.getRoles().contains("USER")) {
            accountRepository.addRole(a.getId(), "BANNED");
            accountRepository.removeRole(a.getId(), "USER");
            if (a.getRoles().contains("MODERATOR")) {
                accountRepository.removeRole(a.getId(), "MODERATOR");
            }
        }
    }

    public void unban(Account a) { // remove BANNED
        if (a.getRoles().contains("BANNED")) {
            accountRepository.addRole(a.getId(), "USER");
            accountRepository.removeRole(a.getId(), "BANNED");
        }
    }

    public void op(Account a) { // give MODERATOR
        if (!a.getRoles().contains("MODERATOR")) {
            accountRepository.addRole(a.getId(), "MODERATOR");
        }
    }

    public void deop(Account a) { // remove MODERATOR
        if (a.getRoles().contains("MODERATOR")) {
            accountRepository.removeRole(a.getId(), "MODERATOR");
        }
    }

}
