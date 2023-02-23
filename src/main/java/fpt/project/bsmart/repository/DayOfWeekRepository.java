package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {
//        List<DayOfWeek> findAllByIds (List<Long> ids);
}
