package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.QuizSubmittion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmittion, Long> {
}
