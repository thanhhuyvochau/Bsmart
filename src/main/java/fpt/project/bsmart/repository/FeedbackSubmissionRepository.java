package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.FeedbackSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackSubmissionRepository extends JpaRepository<FeedbackSubmission, Long> {
    public Page<FeedbackSubmission> findAllByClazz(Class clazz, Pageable pageable);
}
