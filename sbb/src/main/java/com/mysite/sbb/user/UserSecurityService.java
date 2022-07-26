package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/*
 * 스프링 시큐리티에 등록하여 사용할 UserSecurityService는 스프링 시큐리티가 제공하는 UserDetailsService 인터페이스를 구현(implements)해야 한다
 * 스프링 시큐리티의 UserDetailsService는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스이다. 
 * loadUserByUsername 메서드는 사용자명으로 비밀번호를 조회하여 리턴하는 메서드이다.
 */
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);

		if (_siteUser.isEmpty()) {														// 존재하지 않는 사용자 입력시 리턴할 에러
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		SiteUser siteUser = _siteUser.get();
		System.out.println("_siteUser : " + siteUser.getUsername());
		List<GrantedAuthority> authorities = new ArrayList<>();
		/*
		if ("admin".equals(username)) {													// ID가 admin 이면
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));		// UserRole의 ADMIN의 value를 부여
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));		// 일반사용자면 USER의 value를 부여
		}
		*/
		
		
		if (siteUser.getRole().equals("ADMIN")) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else if (siteUser.getRole().equals("SOCIAL")) {
			authorities.add(new SimpleGrantedAuthority(UserRole.SOCIAL.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));		// 일반사용자면 USER의 value를 부여
		}
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
	}
	
	
}
