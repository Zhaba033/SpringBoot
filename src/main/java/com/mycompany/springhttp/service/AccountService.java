package com.mycompany.springhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.springhttp.dto.AccountDTO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class AccountService {
    
    private Map<String, AccountDTO> accounts;
    private final File postsJson = new File("files/json/accounts.json");
    
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    
/*    // CONSTRUCTOR
    public AccountService(ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        om = objectMapper;
        initJson();
    }*/
    
    // PRIVATE METHODS
    private void writeJson() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(postsJson, accounts);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    @PostConstruct
    private void initJson() {
        try {
            accounts = objectMapper.readValue(postsJson, new TypeReference<Map<String, AccountDTO>>() {});
            //log.info(accounts.toString());
        } catch (IOException e) {
            accounts = new HashMap();
            log.error("ERROR: ", e);
        }
    }
    
    // PUBLIC METHODS
    public Map<String, AccountDTO> getAccounts() {
        return accounts;
    }
    
    public List<String> getUserRoles(String username) {
        return accounts.get(username).getRoles();
    }
    
    public void createNewAccount(String username, AccountDTO account) {
        account.setCreatedTime(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.getRoles().add("USER");
        accounts.put(username, account);
        writeJson();
    }
    
    public AccountDTO getUserByName(String name) {
        return accounts.get(name);
    }
    
    public void changePassword(String name, String password) {
        accounts.get(name).setPassword(passwordEncoder.encode(password));
        writeJson();
    }
}
