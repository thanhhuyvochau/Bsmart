package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.QuizSubmittion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmittion, Long> {
    Page<QuizSubmittion> findAll(Specification<QuizSubmittion> quizSubmittionSpecification, Pageable pageable);
}
