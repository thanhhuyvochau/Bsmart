package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByClazz(Class Class) ;

    @Query("SELECT COUNT(*) FROM TimeTable t WHERE t.clazz.id = :classId AND t.date = :date AND t.slot.id = :slotId")
    Long countByClassIdAndDateAndSlotId(@Param("classId") Long classId, @Param("date") Instant date, @Param("slotId") Long slotId);
}
