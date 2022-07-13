package com.mysite.sbb.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	Optional<SiteUser> findByusername(String username);
	
	Optional<SiteUser> findUserByEmail(String email);
	
	/*
	 * ​UPDATE, DELETE 경우 @Transactional 이 어노테이션을 추가해주지 않으면 문제가 발생하기 때문에 아래와 같이 추가해주어야 한다.
	 */
	@Transactional
	@Modifying
	@Query("update SiteUser u set u.password = :temppassword where u.id = :id")
	int updatePassword(@Param("id") Long id, @Param("temppassword") String temppassword);
}
