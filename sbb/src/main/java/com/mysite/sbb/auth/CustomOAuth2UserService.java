package com.mysite.sbb.auth;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	private final HttpSession httpSession;
	private final UserRepository userRepository;
	
	@SneakyThrows
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2UserService");
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		// 현재 로그인 진행 중인 서비스를 구분하는 코드 Google, Naver 등등
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		// oauth2 로그인 진행 시 키가 되는 필드값
		String userNameAttributeName = userRequest.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();
		// OAuthAttributes : attribute를 담을 클래스 (개발자가 생성)
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		
		SiteUser user = saveOrUpdate(attributes);
		// SessioUser: 세션에 사용자 정보를 저장하기 위한 DTO 클래스 (개발자가 생성)
		httpSession.setAttribute("user", new SessionUser(user));
		
		return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
	}
	
    private SiteUser saveOrUpdate(OAuthAttributes attributes) {
    	SiteUser user;
        if(userRepository.findByEmail(attributes.getEmail())!=null){
            user=userRepository.findByEmail(attributes.getEmail());
        }
        else {
            user=attributes.toEntity();
            userRepository.save(user);
            user=userRepository.findByEmail(attributes.getEmail());
        }

        return user;
    }
}
