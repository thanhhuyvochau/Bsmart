package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndStatus(Long id , ECourseStatus status) ;
    Page<Course> findByMentor(User user , Pageable pageable) ;
    Page<Course> findByStatus(ECourseStatus status,  Pageable pageable) ;

    Page<Course> findAll(Specification<Course> build, Pageable pageable);
}
