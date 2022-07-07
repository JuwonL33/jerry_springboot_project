package com.mysite.sbb.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	Page<Answer> findByQuestionId(Integer id, Pageable pageable);

	Page<Answer> findAllByAuthor(Pageable pageable, SiteUser siteUser);
}
