package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {



    Page<Course> findAll(Specification<Course> build, Pageable pageable);


}
