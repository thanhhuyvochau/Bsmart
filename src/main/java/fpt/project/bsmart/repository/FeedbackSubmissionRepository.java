package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.FeedbackSubmission;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackSubmissionRepository extends JpaRepository<FeedbackSubmission, Long> {
    Page<FeedbackSubmission> findAllByClazz(Class clazz, Pageable pageable);
    Optional<FeedbackSubmission> findByClazzAndSubmitBy(Class clazz, User submitBy);
    List<FeedbackSubmission> findAll(Specification<FeedbackSubmission> builder);
}
