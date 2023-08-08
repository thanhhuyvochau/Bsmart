package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackAnswer;
import fpt.project.bsmart.entity.FeedbackQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, Long> {

}
