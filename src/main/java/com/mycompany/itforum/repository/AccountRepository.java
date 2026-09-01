package com.mycompany.itforum.repository;

import com.mycompany.itforum.entity.Account;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Account findByUsername(String Username);
    
    List<Account> findAllByOrderByUsernameAsc();
    
    boolean existsByUsername(String username);
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO account_roles (account_id, priv) VALUES (:account_id, :role);", nativeQuery = true)
    void addRole(@Param("account_id") Long account_id, @Param("role") String role);
    
    @Modifying
    @Transactional
    @Query(value="DELETE FROM account_roles WHERE account_id=:account_id and priv=:role ;", nativeQuery = true)
    void removeRole(@Param("account_id") Long account_id, @Param("role") String role);
    
    
}
