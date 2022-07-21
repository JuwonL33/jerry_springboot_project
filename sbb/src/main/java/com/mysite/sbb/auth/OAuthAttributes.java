package com.mysite.sbb.auth;

import java.util.Map;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
	
	public static OAuthAttributes of(String registrationId,
									 String userNameAttributeName,
									 Map<String, Object> attributes) {
		return ofGoogle(userNameAttributeName, attributes);
	}
	
	public static OAuthAttributes ofGoogle(String userNameAttributeName,
			 							   Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.username((String) attributes.get("username"))
				.email((String) attributes.get("email"))
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
