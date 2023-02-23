package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion,Long> {



}
