package com.mysite.sbb;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.comment.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

	private final AnswerService answerService;
	private final CommentService commentService;
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
	
}
