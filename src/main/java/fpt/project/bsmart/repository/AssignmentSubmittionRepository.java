package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentSubmition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AssignmentSubmittionRepository extends JpaRepository<AssignmentSubmition, Long> {
    @Query("SELECT x FROM AssignmentSubmition x " +
            "JOIN x.assignment a " +
            "JOIN a.activity ac " +
            "JOIN ac.activityAuthorizes aa " +
            "WHERE aa.authorizeClass.id IN :classIds AND x.assignment = :assignment")
    Page<AssignmentSubmition> findAllByAssignment(@Param("assignment") Assignment assignment, @Param("classIds") List<Long> classIds, Pageable pageable);

}
