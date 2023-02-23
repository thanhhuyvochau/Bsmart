package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ForumLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumLessonRepository extends JpaRepository<ForumLesson, Long> {
}
