package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public Answer create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		return answer;
	}
	
	// 답변 페이징 처리 및 정렬
	public Page<Answer> getAnswerList(Integer id, int page)
	{	
		List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));									// 생성일자 순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		return this.answerRepository.findByQuestionId(id, pageable);
	}
	
	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = this.answerRepository.findById(id);
		if (answer.isPresent()) {
			return answer.get();
		} else {
			throw new DataNotFoundException("answer not found");
		}
	}
	
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}
	
	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}
	
	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		this.answerRepository.save(answer);
	}
	
	public Page<Answer> getListByUsername(int page, SiteUser siteUser)
	{
		List<Sort.Order> sorts = new ArrayList<>();									// 생성일자 역순으로 정렬을 위해 sorts 객체 생성
		sorts.add(Sort.Order.desc("createDate"));									// 생성일자 역순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		return this.answerRepository.findAllByAuthor(pageable, siteUser);
	}
	
	/*
	 * 최근 답변 조회
	 */
	public List<Answer> getAnswerListSortByCreateDate()
	{	
		return this.answerRepository.findTop15OrderByCreateDateDesc();
	}
}
