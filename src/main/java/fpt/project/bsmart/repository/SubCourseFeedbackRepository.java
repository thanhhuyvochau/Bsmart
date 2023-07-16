package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.SubCourseFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCourseFeedbackRepository extends JpaRepository<SubCourseFeedback, Long> {
//    Optional<SubCourseFeedback> findBySubCourseAndFeedbackTypeAndFeedbackAnswer_FeedbackUser(SubCourse subCourse, EFeedbackType feedbackType, User feedbackAnswer_feedbackUser);
}
