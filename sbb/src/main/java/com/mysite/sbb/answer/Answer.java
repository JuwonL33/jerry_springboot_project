package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne											// @ManyToOne 애너테이션을 설정하면 Answer 엔티티의 question 속성과 Question 엔티티가 서로 연결. 즉 Answer과 Question간의 Foreign 관계 성립
	private Question question;
	
	@ManyToOne											// 한 명의 사용자가 여러개의 답변을 쓸 수 있음
	private SiteUser author;							// 이 컬럼에는 site_user의 id값이 자동 입력됨
}
