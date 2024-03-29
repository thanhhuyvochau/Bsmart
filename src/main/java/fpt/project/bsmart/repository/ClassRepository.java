package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.FeedbackTemplate;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findAll(Specification<Class> builder);

    Page<Class> findAll(Specification<Class> builder, Pageable pageable);

    Page<Class> findByCourseAndStatus(Course course, ECourseClassStatus status, Pageable pageable);

    List<Class> findByCourseAndStatus(Course course, ECourseClassStatus status);


    List<Class> findByStatus(ECourseClassStatus status);

    List<Class> findByStatus_In(List<ECourseClassStatus> status);

    Page<Class> findByCourse(Course course, Pageable pageable);

    Page<Class> findByMentorAndStatus(User user, ECourseClassStatus status, Pageable pageable);

    Page<Class> findByStatus(ECourseClassStatus status, Pageable pageable);

    List<Class> findByStartDate(Instant startDate);

    List<Class> findByEndDateAndStatus(Instant endDate, ECourseClassStatus status);

    List<Class> findByFeedbackTemplate(FeedbackTemplate feedbackTemplate);

    @Query("SELECT c  FROM Class c INNER JOIN c.studentClasses sc WHERE sc.student  = ?1 and (c.startDate <= ?2 AND c.endDate >= ?2)")
    List<Class> findByStudentAndStartDate(User student, Instant startDate);

    List<Class> findByCourse(Course course);
}
