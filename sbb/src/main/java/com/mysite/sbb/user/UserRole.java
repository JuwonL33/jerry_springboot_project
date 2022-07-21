package com.mysite.sbb.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * - 열거 자료형인 Enumeration 으로 작성. 
 * - 상수자료형이라 Setter는 불필요
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	SOCIAL("ROLE_SOCIAL");		// OAuth
	
	UserRole(String value){
		this.value = value;
	}
	
	private String value;
}
