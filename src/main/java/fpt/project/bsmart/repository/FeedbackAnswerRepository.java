package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackAnswer;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, Long> {
    List<FeedbackAnswer> getAllByFeedbackUserIn(List<User> users);
}
