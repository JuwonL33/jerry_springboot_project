package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.sbb.category.Category;
import com.mysite.sbb.user.SiteUser;

/*
 * DB 테이블에 접근하는 메서드들을 사용하기 위한 인터페이스. CRUD를 어떻게 처리할지를 정의하는 계층.
 * JpaRepository 인터페이스 상속해야 함. 상속시에는 제너릭 타입으로 <리포지토리의 대상이 되는 엔티티 타입과, 해당 엔티티의 PK 타입을 지정해야 함.
 */

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	Question findBySubject(String subject);
	Question findBySubjectAndContent(String subject, String content);
	List<Question> findBySubjectLike(String subject);
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("select "
            + "distinct q "
            + "from Question q " 
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "left outer join Category c on q.category=c "
            + "where "
            + "   q.category = :category"
            + "   and (q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw%)")
	Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable, @Param("category") Category category);
	
	Page<Question> findAllByAuthor(Pageable pageable, SiteUser siteUser);
	
	Page<Question> findAllByCategory(Specification<Question> spec, Pageable pageable, Category category);
	
	@Modifying
	@Query("update Question q set q.view = q.view + 1 where q.id = :id")
	int updateView(@Param("id") Integer id);
}