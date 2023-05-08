package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackQuestionRepository extends JpaRepository<FeedbackQuestion, Long> {
    Long countByIdIn(List<Long> idList);
}
