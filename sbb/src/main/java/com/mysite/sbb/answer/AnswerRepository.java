package com.mysite.sbb.answer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mysite.sbb.user.SiteUser;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	Page<Answer> findByQuestionId(Integer id, Pageable pageable);

	Page<Answer> findAllByAuthor(Pageable pageable, SiteUser siteUser);
	
	@Query(value = "SELECT * FROM ANSWER ORDER BY CREATE_DATE DESC LIMIT 15", nativeQuery = true)
	List<Answer> findTop15OrderByCreateDateDesc();
}
