package com.mycompany.itforum.service;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.repository.AccountRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{
    
    private final AccountRepository accountRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = null;
        List<String> roles = null;
        
        if (accountRepository.existsByUsername(username)) {
            Account user = accountRepository.findByUsername(username);
            password = user.getPassword();
            roles = user.getRoles();
        } else {    
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }
        
        String[] rolesArray = new String[roles.size()];
        roles.toArray(rolesArray);
        
        return User.builder()
            .username(username)
            .password(password)
            .roles(rolesArray)
            .build();
    }
}
