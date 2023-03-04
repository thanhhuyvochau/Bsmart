package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCourseBySubject(Subject subject);
}
