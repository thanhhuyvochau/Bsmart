package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByIdAndType(Long id, ECourseActivityType type);

    List<Activity> findByCourseAndParentIdIsNull(Course course);

    List<Activity> findByCourseAndParentId(Course course, Long parentId);
}
