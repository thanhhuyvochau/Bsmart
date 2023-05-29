package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {

    DayOfWeek  findByCode(EDayOfWeekCode code ) ;
}
