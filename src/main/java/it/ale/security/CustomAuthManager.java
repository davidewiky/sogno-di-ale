package it.ale.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.ale.exception.InvalidLoginException;
import it.ale.model.Account;
import it.ale.repository.AccountReposistory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthManager implements AuthenticationProvider {

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AccountReposistory accountReposistory;

    public CustomAuthManager(UserDetailService userDetailService, PasswordEncoder passwordEncoder,
                             AccountReposistory accountReposistory) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
        this.accountReposistory = accountReposistory;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Account account = accountReposistory.findAccountByUserName(username);
        if (account != null &&
                (pwd.equals(account.getPassword()) || passwordEncoder.matches(pwd, account.getPassword()))) {
            UserDetails user = userDetailService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, passwordEncoder.encode(pwd), user.getAuthorities());
        }
        throw new InvalidLoginException(username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
