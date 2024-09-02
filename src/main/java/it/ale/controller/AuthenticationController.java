package it.ale.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.ale.model.Account;
import it.ale.security.JwtService;
import it.ale.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.ale.exception.InvalidLoginException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Authentication controller", description = "Validation user")
@RequestMapping(path = { "/api/auth" })
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;

	public AuthenticationController(AuthenticationManager authenticationManager,
									JwtService jwtService, PasswordEncoder passwordEncoder,
									AccountService accountService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.accountService = accountService;
	}

	@PostMapping(value = "/register")
	public ResponseEntity<Map<String, String>> createInternalUserLogin(@RequestBody Map<String, Object> body) throws InvalidLoginException, JsonProcessingException {
		String username = (String) body.get("username");
		String pwd = (String) body.get("password");
		Account account = accountService.registerAccount(username, passwordEncoder.encode(pwd));
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getUserName(), account.getPassword()));
		if (authentication.isAuthenticated()) {
			return getMapResponseAuthenticated(authentication, account.getPassword());
		}
		throw new InvalidLoginException(username);
	}

	private ResponseEntity<Map<String, String>> getMapResponseAuthenticated(Authentication authentication, String pwd) {
		String token = jwtService.generateToken(((UserDetails) authentication.getPrincipal()).getUsername(), pwd);
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		response.put("message", "success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<Map<String, String>> internalUserLogin(@RequestBody Map<String, Object> body) throws InvalidLoginException, JsonProcessingException {
		String username = (String) body.get("username");
		String pwd = (String) body.get("password");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pwd));
		if (authentication.isAuthenticated()) {
			return getMapResponseAuthenticated(authentication, passwordEncoder.encode(pwd));
		}
		throw new InvalidLoginException(username);
	}

}
