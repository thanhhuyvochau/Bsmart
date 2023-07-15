package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    Page<Course> findAll(Specification<Course> build, Pageable pageable);

    Page<Course> findByCreator(User user, Pageable pageable);
    Optional<Course> findByIdAndStatus(Long id, ECourseStatus status);
}
