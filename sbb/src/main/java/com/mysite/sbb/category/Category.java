package com.mysite.sbb.category;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.mysite.sbb.question.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;														// 카테고리 아이디
	
	@Column(unique = true)
	private String label;													// 카테고리 레이블
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	private List<Question> questionList;
}
