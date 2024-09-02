package it.ale.repository;

import it.ale.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountReposistory extends JpaRepository<Account, String> {
    Account findAccountByUserName(String userName);
}
