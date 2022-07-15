package com.mysite.sbb;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final CommentService commentService;
    private final UserService userService;
    
    @Value("${spring.mail.username}")
    private String from;
    
	/*
	 * 최근 답변 및 최근 댓글 확인을 위한 메소드
	 */
	@RequestMapping("/latest")
	public String latest(Model model) {
		// TODO : 최근 답변 및 댓글을 가져오는 메서드 구현
		List<Answer> answerList = this.answerService.getAnswerListSortByCreateDate();
		List<Comment> commentList = this.commentService.getCommentListSortByCreateDate();

		model.addAttribute("answerList", answerList);
		model.addAttribute("commentList", commentList);

		return "/common/latest";
	}
	
	/*
	 * GET : 비밀번호를 찾기 위한 메소드
	 */
	@RequestMapping("/forgot_password")
	public String forgot_password(Model model, ForgotPasswordForm forgotPasswordForm) {
		return "/common/forgot_password";
	}
	
	/*
	 * POST : 비밀번호를 찾기 위한 메소드
	 */
	@Transactional
	@PostMapping("/forgot_password")
	public String forgot_password(@Valid ForgotPasswordForm forgotPasswordForm, BindingResult bindingResult) {	
		if (bindingResult.hasErrors()) {
            return "/common/forgot_password";
        }
		
		try {
			SiteUser user = this.userService.findUserByEmail(forgotPasswordForm.getEmail());
			Mail mail = this.userService.createMail(forgotPasswordForm.getEmail());
			this.userService.mailSend(mail);
			return "/login_form";
		} catch (DataNotFoundException e) {
	        // 이메일에 해당하는 계정 존재여부 체크
        	e.printStackTrace();
            bindingResult.rejectValue("email", "emailInCorrect", "해당 이메일이 존재하지 않습니다.");
            return "/common/forgot_password";
        } 


	}
	
	@RequestMapping("/privacy")
	public String privacy(Model model) {
		Question privacy = this.questionService.getQuestion(616);
		model.addAttribute("privacy", privacy);
		return "/common/privacy";
	}
}
