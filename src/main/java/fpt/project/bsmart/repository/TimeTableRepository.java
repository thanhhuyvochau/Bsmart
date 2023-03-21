package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
}
