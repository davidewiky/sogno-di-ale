package it.ale.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public InvalidLoginException(String user) {
		super("Invalid credential provided");
	}
}
