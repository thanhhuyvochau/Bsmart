package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudentClassKeyId(StudentClassKey studentClassKey);

    List<Attendance> findAllByStudentClassKeyIdIn(List<StudentClassKey> studentClassKeys);
//    List<Attendance> findAllByStudentClassKeyIdIn(List<StudentClassKey> studentClassKeys ) ;

    List<Attendance> findAllByTimeTableIn(List<TimeTable> timeTables);

}
