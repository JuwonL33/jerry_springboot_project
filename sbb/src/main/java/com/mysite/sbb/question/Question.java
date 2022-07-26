package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity								// 모델 또는 도메인 모델이라고 부르기도 함. 여기선 이것을 구분하지 않고 테이블과 매핑되는 클래스를 엔티티라 할 거임.
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")							// columnDefinition = "TEXT"은 "내용"처럼 글자 수를 제한할 수 없는 경우에 사용
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;							// 수정일시 컬럼 추가
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	@ManyToOne													// 한 명의 사용자가 여러개의 질문글을 작성할 수 있음
	private SiteUser author;									// 이 컬럼에는 site_user의 id값이 자동 입력됨
	
	@ManyToMany													// 질문 하나에 여러 사람이 추천할 수 있고, 한 사람은 여러개의 질문을 추천할 수 있음. 이럴땐 대등한 관계 ManyToMany를 써야함. 
	Set<SiteUser> voter;										// List가 아닌 Set인 이유 : 한 사람 당 한 번만 추천할 수 있으므로

	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)	// 질문 하나에 여러 사람이 댓글달 수 있고, 한 사람은 여러개의 질문을 추천할 수 있음. 이럴땐 대등한 관계 ManyToMany를 써야함. 
	List<Comment> commentList;										// 이 경우엔 새로운 테이블이 생긴다. List인 이유는 똑같은 내용으로 댓글을 달 수 있을테니까. 
	
	@ManyToOne
	private Category category;
	
	@Column(columnDefinition = "integer default 0", nullable = false)	// 조회수
	private int view;
	
	@Column(columnDefinition = "integer default 0", nullable = false)	// 조회수
	private int recommended;
}
