package it.ale.security;

import it.ale.model.Account;
import it.ale.repository.AccountReposistory;
import it.ale.service.AccountService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {

    private final AccountService accountService;
    private Set<GrantedAuthority> set = new HashSet<>();

    public UserDetailService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.getAccount(username);
        GrantedAuthority authorities = new SimpleGrantedAuthority(account.getRole());
        set.add(authorities);
        return new User(account.getUserName(), account.getPassword(), set);
    }
}
