package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackTemplateRepository extends JpaRepository<FeedbackTemplate, Long> {
}
