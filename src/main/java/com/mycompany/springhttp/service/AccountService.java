package com.mycompany.springhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.springhttp.dto.AccountDTO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AccountService {
    
    private final ObjectMapper om;
    private Map<String, AccountDTO> accounts;
    private final File postsJson = new File("files/json/accounts.json");
    
    // CONSTRUCTOR
    public AccountService(ObjectMapper objectMapper) {
        om = objectMapper;
        initJson();
    }
    
    // PRIVATE METHODS
    private void writeJson() {
        try {
            om.writerWithDefaultPrettyPrinter()
                    .writeValue(postsJson, accounts);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void initJson() {
        try {
            accounts = om.readValue(postsJson, new TypeReference<Map<String, AccountDTO>>() {});
            //log.info(accounts.toString());
        } catch (IOException e) {
            accounts = new HashMap();
            log.error("ERROR: ", e);
        }
    }
    
    // PUBLIC METHODS
    public Map<String, AccountDTO> getUserData() {
        return accounts;
    }
    
    public void createNewAccount(String username, AccountDTO account) {
        account.setCreatedTime(LocalDateTime.now());
        account.add_role("USER");
        accounts.put(username, account);
        writeJson();
    }
    
    public AccountDTO getUserByName(String name) {
        return accounts.get(name);
    }
}
