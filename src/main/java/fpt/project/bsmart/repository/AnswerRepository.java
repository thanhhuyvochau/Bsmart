package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {


  Optional<Answer> findByIdAndSurveyQuestion(Long answerId , SurveyQuestion surveyQuestion );
}
