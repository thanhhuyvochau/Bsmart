package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.TimeInWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeInWeekRepository extends JpaRepository<TimeInWeek, Long> {
}
