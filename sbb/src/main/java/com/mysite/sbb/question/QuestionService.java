package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	// 검색기능
	private Specification<Question> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);	// 중복 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(
					   cb.like(q.get("subject"), "%" + kw +"%"),		// 제목
					   cb.like(q.get("content"), "%" + kw +"%"),		// 내용
					   cb.like(u1.get("username"), "%" + kw +"%"),		// 질문작성자
					   cb.like(a.get("content"), "%" + kw +"%"),		// 답변내용
					   cb.like(u2.get("username"), "%" + kw +"%"));		// 답변작성자
			}
		};
	}
	
	public Page<Question> getList(int page, String kw, Category category, String sort)
	{

		Question question = new Question();
		System.out.println(sort.trim() == "recommend");
		List<Sort.Order> sorts = new ArrayList<>();										// 정렬을 위해 sorts 객체 생성
		if (sort.equals("latest")) {
			sorts.add(Sort.Order.desc("createDate"));									// 최신순으로 정렬
		} else if (sort.equals("view")) {												// 조회수가 높은 순으로 정렬
			sorts.add(Sort.Order.desc("view"));											// 최신순으로 정렬
			sorts.add(Sort.Order.desc("createDate"));	
		} else {																		// 추천이 많은순으로 정렬
			sorts.add(Sort.Order.desc("recommended"));		
			sorts.add(Sort.Order.desc("createDate"));	
		}
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		// Specification<Question> spec = search(kw);
		return this.questionRepository.findAllByKeyword(kw, pageable, category);
	}
	
	/*
	public Page<Question> getList(int page, String kw)
	{
		List<Sort.Order> sorts = new ArrayList<>();									// 생성일자 역순으로 정렬을 위해 sorts 객체 생성
		sorts.add(Sort.Order.desc("createDate"));									// 생성일자 역순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		Specification<Question> spec = search(kw);
		return this.questionRepository.findAllByKeyword(kw, pageable);
	}
	*/
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		}else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	public void create(String subject, String content, SiteUser user, Category category) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		q.setCategory(category);
		this.questionRepository.save(q);
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	@Transactional
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
		int id = question.getId();
		this.questionRepository.updateRecommend(id);
	}
	
	public Page<Question> getListByUsername(int page, SiteUser siteUser)
	{
		List<Sort.Order> sorts = new ArrayList<>();									// 생성일자 역순으로 정렬을 위해 sorts 객체 생성
		sorts.add(Sort.Order.desc("createDate"));									// 생성일자 역순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		// Specification<Question> spec = search(kw);
		return this.questionRepository.findAllByAuthor(pageable, siteUser);
	}
	
	/* Views Counting */
	@Transactional
	public int updateView(Integer id) {
		return this.questionRepository.updateView(id);
	}
}