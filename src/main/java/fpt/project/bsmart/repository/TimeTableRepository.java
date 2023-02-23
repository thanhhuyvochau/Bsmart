package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.TimeTable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
        List<TimeTable> findAll (Specification<TimeTable> spec) ;

}
