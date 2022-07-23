package com.mysite.sbb.exception;

/*
 * 가입 실패
 */
public class OAuth2RegistrationException extends RuntimeException {
	public OAuth2RegistrationException(String message) {
		super(message);
	}
}
