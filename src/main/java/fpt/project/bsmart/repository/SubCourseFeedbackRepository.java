package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.SubCourseFeedback;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCourseFeedbackRepository extends JpaRepository<SubCourseFeedback, Long> {
    Optional<SubCourseFeedback> findBySubCourseAndFeedbackTypeAndFeedbackAnswer_FeedbackUser(SubCourse subCourse, EFeedbackType feedbackType, User feedbackAnswer_feedbackUser);
}
