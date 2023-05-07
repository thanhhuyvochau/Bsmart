package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllBySubject(Subject subject, Pageable pageable);
}
