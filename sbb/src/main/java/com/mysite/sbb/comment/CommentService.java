package com.mysite.sbb.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	
	public Comment getComment(Integer id) {
		Optional<Comment> comment = this.commentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		} else {
			throw new DataNotFoundException("comment not found");
		} 
	}
	 
	public Comment createQuestionComment(Question question, String content, SiteUser author) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setQuestion(question);
		comment.setAuthor(author);
		this.commentRepository.save(comment);
		return comment;
	}
	
	public Comment createAnswerComment(Answer answer, String content, SiteUser author) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setAnswer(answer);
		comment.setAuthor(author);
		this.commentRepository.save(comment);
		return comment;
	}
	
	public void modify(Comment comment, String content) {
		comment.setContent(content);
		comment.setModifyDate(LocalDateTime.now());
		this.commentRepository.save(comment);
	}
	
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}
	
	public Page<Comment> getListByUsername(int page, SiteUser siteUser)
	{
		List<Sort.Order> sorts = new ArrayList<>();									// 생성일자 역순으로 정렬을 위해 sorts 객체 생성
		sorts.add(Sort.Order.desc("createDate"));									// 생성일자 역순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));				// page 번호를 param으로 받는다. Pageable 객체를 반환.
		return this.commentRepository.findAllByAuthor(pageable, siteUser);
	}
	
	/*
	 * 최근 댓글 조회
	 */
	public List<Comment> getCommentListSortByCreateDate()
	{	
		return this.commentRepository.findTop15OrderByCreateDateDesc();
	}
}
