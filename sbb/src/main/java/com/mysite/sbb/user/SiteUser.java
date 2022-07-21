package com.mysite.sbb.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.comment.Comment;
import com.mysite.sbb.question.Question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SiteUser {			// 엔티티명을 User대신 SiteUser라고 지은 이유 : 스프링 시큐리티에 이미 User 클래스가 있기 때문이다.
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	private UserRole role;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)		// 한 회원이 여러개의 질문을 쓸 수 있음
	List<Question> userQuestionList;		
	

	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)		// 한 회원이 여러개의 답변을 쓸 수 있음
	List<Answer> userAnswerList;	

	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)		// 한 회원이 여러개의 댓글을 쓸 수 있음
	List<Comment> userCommentList;	
	
	
	@Builder
	public SiteUser(String username, String email, UserRole role) {
		this.username = username;
		this.email = email;
		this.role = role;
	}
	
	public SiteUser update(String username) {
		this.username = username;
		return this;
	}
	
	public String getRoleKey() {
		return this.role.getValue();
	}
}
