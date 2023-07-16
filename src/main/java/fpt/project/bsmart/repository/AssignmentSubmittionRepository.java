package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.AssignmentSubmition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AssignmentSubmittionRepository extends JpaRepository<AssignmentSubmition, Long> {
}
