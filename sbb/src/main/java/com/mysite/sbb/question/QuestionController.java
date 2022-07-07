package com.mysite.sbb.question;

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
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.CommentForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {

	public final QuestionService questionService;
	public final AnswerService answerService;
	public final UserService userService;
	
	@RequestMapping("/list")
	// http://localhost:8080/question/list?page=0 처럼 GET 방식으로 요청된 URL에서 page값을 가져오기 위해 @RequestParam 처리
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="kw", defaultValue="") String kw) {
		log.info("page:{}, kw:{}", page, kw);
		Page<Question> paging = this.questionService.getList(page, kw);
		model.addAttribute("paging", paging);			// 이전에 가지고 있었던 paging 값을 기억해뒀다가 다시 돌려줌
		model.addAttribute("kw", kw);					// 이전에 가지고 있었던 kw 값을 기억해뒀다가 다시 돌려줌
		return "/question/question_list";
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(Model model, 
			@PathVariable("id") Integer id, AnswerForm answerForm, CommentForm commentForm, 
			@RequestParam(value="page", defaultValue="0") int page) {
		Question question = this.questionService.getQuestion(id);
		Page<Answer> answerPaging = this.answerService.getAnswerList(id, page);				// answerList는 페이징처리하여 별도로 조회하기
		model.addAttribute("question", question);
		model.addAttribute("answerPaging", answerPaging);
		return "/question/question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")				// 로그인이 필요한 메서드. 로그인 안되어 있으면 로그인 페이지로 이동시킴
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "/question/question_form";
	}
	
	@PreAuthorize("isAuthenticated()")				// 로그인이 필요한 메서드. 로그인 안되어 있으면 로그인 페이지로 이동시킴
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "/question/question_form";
		}
		
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list";	// 질문 저장 후 질문목록으로 이동
	}
	
	@PreAuthorize("isAuthenticated()")				// 로그인이 필요한 메서드. 로그인 안되어 있으면 로그인 페이지로 이동시킴
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())){				// 세션에 있는 사용자와 다른 사용자가 수정 요청했을 때
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "/question/question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "/question/question_form";
		}
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.questionService.delete(question);
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
}
