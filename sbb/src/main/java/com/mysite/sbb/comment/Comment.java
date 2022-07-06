package com.mysite.sbb.comment;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity							
// @Entity 애너테이션을 적용해야 JPA가 엔티티로 인식
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)			// @GeneratedValue : sequence처럼 1씩 자동으로 증가. / GenerationType.IDENTITY는 해당 컬럼만의 독립적인 시퀀스를 생성하여 번호를 증가시킬 때 사용
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;							// 수정일시 컬럼 추가
	
	@ManyToOne													// @ManyToOne 애너테이션을 설정하면 Comment 엔티티의 question 속성과 Question 엔티티가 서로 연결. 즉 Comment과 Question간의 Foreign 관계 성립
	private Question question;
	
	@ManyToOne													// @ManyToOne 애너테이션을 설정하면 Comment 엔티티의 question 속성과 Question 엔티티가 서로 연결. 즉 Comment과 Question간의 Foreign 관계 성립
	private Answer answer;
	
	@ManyToOne													// 한 명의 사용자가 여러개의 댓글을 쓸 수 있음
	private SiteUser author;									// 이 컬럼에는 site_user의 id값이 자동 입력됨
	
	@ManyToMany													// 질문 하나에 여러 사람이 추천할 수 있고, 한 사람은 여러개의 질문을 추천할 수 있음. 이럴땐 대등한 관계 ManyToMany를 써야함. 
	Set<SiteUser> voter;										// 이 경우엔 새로운 테이블이 생긴다.
}
