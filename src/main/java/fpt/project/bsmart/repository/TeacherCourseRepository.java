package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, TeacherCourseKey> {
    List<TeacherCourse> findAllByCourse (Course course ) ;


}
