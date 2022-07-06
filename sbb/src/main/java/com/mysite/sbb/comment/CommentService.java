package com.mysite.sbb.comment;

import java.time.LocalDateTime;
import java.util.Optional;

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
}
