package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackQuestion;
import fpt.project.bsmart.entity.FeedbackTemplate;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackQuestionsRepository extends JpaRepository<FeedbackQuestion, Long> {

}
