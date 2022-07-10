package com.mysite.sbb.comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Page<Comment> findAllByAuthor(Pageable pageable, SiteUser siteUser);
	
	@Query(value = "SELECT * FROM COMMENT ORDER BY CREATE_DATE DESC LIMIT 15", nativeQuery = true)
	List<Comment> findTop15OrderByCreateDateDesc();
}
