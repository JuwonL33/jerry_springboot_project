package com.mysite.sbb.category;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public Category getCategory(Integer id) {
		Optional<Category> category = this.categoryRepository.findById(id);
		if(category.isPresent()) {
			return category.get();
		}else {
			throw new DataNotFoundException("category not found");
		}
	}
	
	public Category findCategoryByLabel(String label) {
		return this.categoryRepository.findByLabel(label);
	}
	
	
	public Category create(String label) {
		Category category = new Category();
		category.setLabel(label);
		this.categoryRepository.save(category);
		return category;
	}
}
