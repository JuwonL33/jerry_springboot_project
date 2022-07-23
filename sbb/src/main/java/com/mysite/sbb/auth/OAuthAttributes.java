package com.mysite.sbb.auth;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.sbb.exception.OAuth2RegistrationException;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey, username, email;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, 
			String nameAttributeKey, 
			String username, 
			String email) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.username = username;
		this.email = email;
		
	}
	
	@SneakyThrows
	public static OAuthAttributes of(String registrationId,
									 String userNameAttributeName,
									 Map<String, Object> attributes) {
		log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userNameAttributeName));
		log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(attributes));
		String registrationIdToLower = registrationId.toLowerCase();
		switch (registrationIdToLower) {
			case "google" : return ofGoogle(registrationId, userNameAttributeName, attributes);
			case "facebook" : return ofFacebook(registrationId, userNameAttributeName, attributes);
			case "naver" : return ofNaver(registrationId, userNameAttributeName, attributes);
			case "kakao" : return ofKakao(registrationId, userNameAttributeName, attributes);
			default : throw new OAuth2RegistrationException("해당 소셜 로그인은 현재 지원하지 않습니다.");
		}

	}
	
	public static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName,
			 							   Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.username((String) attributes.get(userNameAttributeName))
				.email((String) attributes.get("email"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.build();
	}
	
	public static OAuthAttributes ofFacebook(String registrationId, String userNameAttributeName,
					   Map<String, Object> attributes) {
		return OAuthAttributes.builder()
		.username((String) attributes.get("id"))
		.email((String) attributes.get("email"))
		.attributes(attributes)
		.nameAttributeKey(userNameAttributeName)
		.build();
		}
	
	/*	네이버일때 리턴하는 데이터
		{resultcode=00, 
		message=success, 
		response=
		{id=hhNqr6ALZ80nirgTnUpa8P4SUQzS94UkO8re3_XIja4, nickname=Ashley, email=leejw2013@naver.com}}
	 */
	public static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName,
					   Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributes.builder()
		.username((String) response.get("id"))
		.email((String) response.get("email"))
		.attributes(response)
		.nameAttributeKey("id")
		.build();
		}

	public static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName,
			   Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		return OAuthAttributes.builder()
		.username(attributes.get(userNameAttributeName).toString())
		.email((String) account.get("email"))
		.attributes(attributes)
		.nameAttributeKey(userNameAttributeName)
		.build();
		}
	
	public SiteUser toEntity() {
		return SiteUser.builder()
				.username(username)
				.email(email)
				.role(UserRole.SOCIAL)
				.build();
	}

}
