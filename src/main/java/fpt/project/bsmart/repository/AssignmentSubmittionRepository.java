package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentFile;
import fpt.project.bsmart.entity.AssignmentSubmition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AssignmentSubmittionRepository extends JpaRepository<AssignmentSubmition, Long> {
    Page<AssignmentSubmition> findAllByAssignment(Assignment assignment, Pageable pageable);

}
