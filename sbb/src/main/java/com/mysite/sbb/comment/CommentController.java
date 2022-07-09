package com.mysite.sbb.comment;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	// Post : Create Question Comment
	@PostMapping("/create/question/{id}")
	public String createQuestionComment(Model model, @PathVariable("id") Integer id, Principal principal, @Valid CommentForm commentForm, BindingResult bindingResult) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		if (bindingResult.hasErrors()) {
            return "comment_form";
        }
		
		this.commentService.createQuestionComment(question, commentForm.getContent(), siteUser);
		
		return String.format("redirect:/question/detail/%s/%s", question.getCategory().getLabel(), question.getId());
	}
	
	// Post : Create Answer Comment
	@PostMapping("/create/answer/{id}")
	public String createAnswerComment(Model model, @PathVariable("id") Integer id, Principal principal, @Valid CommentForm commentForm, BindingResult bindingResult) {
		System.out.println("id : " + id);
		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		if (bindingResult.hasErrors()) {
            return "comment_form";
        }
		
		this.commentService.createAnswerComment(answer, commentForm.getContent(), siteUser);
		
		return String.format("redirect:/question/detail/%s/%s", answer.getQuestion().getCategory().getLabel() ,answer.getQuestion().getId());
	}
	
	// Get : Modify Comment
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String commentModify(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		commentForm.setContent(comment.getContent());
		return "comment_form";
	}
	
	// Post : Modify Comment
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
            return "comment_form";
        }
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        if (comment.getQuestion() != null) {
        	return String.format("redirect:/question/detail/%s/%s", comment.getQuestion().getCategory().getLabel(), comment.getQuestion().getId());
        } else {
        	return String.format("redirect:/question/detail/%s/%s", comment.getAnswer().getQuestion().getCategory().getLabel(), comment.getAnswer().getQuestion().getId());
        }
        
    }
	
	// Get : Delete Comment
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String commentDelete(@PathVariable("id") Integer id, Principal principal) {
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.commentService.delete(comment);
        if (comment.getQuestion() != null) {
        	return String.format("redirect:/question/detail/%s/%s", comment.getQuestion().getCategory().getLabel(), comment.getQuestion().getId());
        } else {
        	return String.format("redirect:/question/detail/%s/%s", comment.getAnswer().getQuestion().getCategory().getLabel(), comment.getAnswer().getQuestion().getId());
        }
	}
}
