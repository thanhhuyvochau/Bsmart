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

    Page<SubCourse> findByCourseAndStatus(Course course ,ECourseStatus status, Pageable pageable) ;

    Page<SubCourse> findByStatus(ECourseStatus status , Pageable pageable) ;

    Page<SubCourse> findByStatusNot(ECourseStatus status ,Pageable pageable) ;
    Page<SubCourse> findByStatusAndMentor(ECourseStatus status, User user, Pageable pageable);

    Page<SubCourse> findByMentor( User user, Pageable pageable);
    Optional<SubCourse> findByIdAndStatus(Long id , ECourseStatus status) ;
    Page<SubCourse> findAll(Specification<SubCourse> build, Pageable pageable);
}
