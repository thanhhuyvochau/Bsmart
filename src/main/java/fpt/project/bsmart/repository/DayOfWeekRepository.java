package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface DayOfWeekRepository  extends JpaRepository<DayOfWeek, Long> {
}
