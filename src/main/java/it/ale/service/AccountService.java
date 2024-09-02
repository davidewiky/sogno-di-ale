package it.ale.service;

import it.ale.model.Account;
import it.ale.repository.AccountReposistory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountReposistory accountReposistory;

    public AccountService(AccountReposistory accountReposistory) {
        this.accountReposistory = accountReposistory;
    }

    public Account getAccount(String username) {
        return accountReposistory.findAccountByUserName(username);
    }

    public Account registerAccount(String username, String pwd) {
        Account account = new Account();
        account.setRole("ADMIN");
        account.setUserName(username);
        account.setPassword(pwd);
        return accountReposistory.save(account);
    }
}
