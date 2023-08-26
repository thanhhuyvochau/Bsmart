package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    Optional<StudentClass> findByClazzAndStudent(Class clazz, User student);
    List<StudentClass> findByStudent(User student);

    List<StudentClass> findByClazz(Class aClass);
    @Query("SELECT sc FROM StudentClass AS sc where sc.clazz.course.id = :courseId AND sc.student.id = :userId")
    Optional<StudentClass> findByCourseAndStudent(@Param("courseId") Long courseId, @Param("userId") Long userId);
}
