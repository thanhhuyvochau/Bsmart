package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackTemplate;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackTemplateRepository extends JpaRepository<FeedbackTemplate, Long> {
    Page<FeedbackTemplate> findAll(Specification<FeedbackTemplate> build, Pageable pageable);

    FeedbackTemplate findByTypeAndIsDefault(EFeedbackType type, Boolean isDefault);
}
