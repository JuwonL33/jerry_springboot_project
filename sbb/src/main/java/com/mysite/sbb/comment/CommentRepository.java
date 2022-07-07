package com.mysite.sbb.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.user.SiteUser;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Page<Comment> findAllByAuthor(Pageable pageable, SiteUser siteUser);
}
