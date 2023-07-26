package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentFile;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Long> {
}
