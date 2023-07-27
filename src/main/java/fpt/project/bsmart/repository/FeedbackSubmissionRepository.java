package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackSubmissionRepository extends JpaRepository<FeedbackSubmission, Long> {
}
