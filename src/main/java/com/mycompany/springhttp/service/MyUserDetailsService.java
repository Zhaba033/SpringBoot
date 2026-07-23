package com.mycompany.springhttp.service;

import com.mycompany.springhttp.dto.AccountDTO;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{
    
    private final AccountService accountService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = null;
        List<String> roles = null;

        
        Map<String, AccountDTO> accounts = accountService.getAccounts();
        
        if (accounts.containsKey(username)) {
            password = accounts.get(username).getPassword();
            roles = accounts.get(username).getRoles();
        } else {    
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }
        
        String[] rolesArray = new String[roles.size()];
        roles.toArray(rolesArray);
//        log.info(String.valueOf(rolesArray.length));
//        log.info(rolesArray[0]);
        
        return User.builder()
            .username(username)
            .password(password)
            .roles(rolesArray)
            .build();
    }
}
