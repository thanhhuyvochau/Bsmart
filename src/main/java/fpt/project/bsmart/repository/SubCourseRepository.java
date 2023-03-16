package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubCourseRepository extends JpaRepository<SubCourse, Long> {

    Optional<SubCourse> findByIdAndStatus(Long id , ECourseStatus status) ;
    Page<SubCourse> findAll(Specification<SubCourse> build, Pageable pageable);
}
