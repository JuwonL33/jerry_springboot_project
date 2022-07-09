package com.mysite.sbb;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.category.CategoryService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Test
	void contextLoads() {
		/*
		List<Question> questionList = this.questionService.getAllQuestion();
		// Category category = this.categoryService.getCategory(2);
		System.out.println("questionList : " + questionList);
		
		for(Question question: questionList) {
		    this.questionService.update(question, category);
		}*/
	}

}