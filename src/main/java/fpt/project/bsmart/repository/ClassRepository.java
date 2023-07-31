package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    Page<Class> findAll (Specification<Class> builder, Pageable pageable);
    List<Class> findAll(Specification<Class> builder);

    Page<Class> findByCourseAndStatus(Course course , ECourseStatus status, Pageable pageable) ;

    List<Class> findByCourseAndStatus(Course course , ECourseStatus status) ;

    Page<Class> findByCourse(Course course,  Pageable pageable) ;

    Page<Class> findByMentorAndStatus(User user , ECourseStatus status,  Pageable pageable) ;
    Page<Class> findByStatus(ECourseStatus status,  Pageable pageable) ;

    List<Class> findByStatus(ECourseStatus status) ;
}
