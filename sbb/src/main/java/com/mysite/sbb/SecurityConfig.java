package com.mysite.sbb;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.auth.CustomOAuth2UserService;
import com.mysite.sbb.user.UserSecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserSecurityService userSecurityService;
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@SuppressWarnings("deprecation")
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().antMatchers("/**").permitAll()
		.and()
			.csrf().ignoringAntMatchers("/h2-console/**")						// h2-console 접속시 403 Forbidden이 뜨는 것을 방지
		.and()
			.headers()
			.addHeaderWriter(new XFrameOptionsHeaderWriter(
					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))	// h2-console 접속시 화면 깨짐을 방지. 화면깨짐의 원인 :  H2 콘솔의 화면이 frame 구조로 작성,스프링 시큐리티는 사이트의 콘텐츠가 다른 사이트에 포함되지 않도록 하기 위해 X-Frame-Options 헤더값을 사용하여 이를 방지 
		.and()
			.formLogin()
			.loginPage("/user/login")											// 로그인 페이지 URL
			.defaultSuccessUrl("/")												// 로그인 성공시 이동할 디폴트 URL
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))	// 로그아웃 URL 설정
			.logoutSuccessUrl("/")												// 로그아웃 성공 시 루트로 이동
			.invalidateHttpSession(true)										// 로그아웃시 생성된 사용자 세션도 삭제하도록 처리
		.and()
			.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * SpringSecurity의 configure 메서드를 오버라이딩. 
	 * AuthenticationManagerBuilder : 스프링 시큐리티의 인증을 담당
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());		// auth 객체에 userSecurityService를 등록하여 사용자 조회를 담당하도록 설정. passwordEncoder도 함께 등록해 비밀번호 검증에도 사용한다. 
	}
}
