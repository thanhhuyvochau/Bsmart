package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Quiz;
import fpt.project.bsmart.entity.QuizSubmittion;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmittion, Long> {
    Page<QuizSubmittion> findAll(Specification<QuizSubmittion> quizSubmittionSpecification, Pageable pageable);
    List<QuizSubmittion> findAllByQuizAndSubmittedBy(Quiz quiz, User user);
}
