package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Lesson;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByActivityIdIn(List<Long> activityId) ;

}
