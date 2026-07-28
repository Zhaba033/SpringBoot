package com.mycompany.itforum.repository;

import com.mycompany.itforum.entity.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Account findByUsername(String Username);
    Account findByCommentsId(Long id);
    
    boolean existsByUsername(String username);
    
    
}
