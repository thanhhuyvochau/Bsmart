package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedbackSubmissionSpecificationBuilder {
    public static FeedbackSubmissionSpecificationBuilder feedbackSubmissionSpecificationBuilder(){return new FeedbackSubmissionSpecificationBuilder();}
    private List<Specification<FeedbackSubmission>> specifications = new ArrayList();

    public FeedbackSubmissionSpecificationBuilder filterByCourse(Long courseId){
        if(courseId == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<FeedbackSubmission, Class> feedbackSubmissionClassJoin = root.join(FeedbackSubmission_.CLAZZ);
            Join<Class, Course> classCourseJoin = feedbackSubmissionClassJoin.join(Class_.COURSE);
            return criteriaBuilder.equal(classCourseJoin.get(Course_.ID), courseId);
        });
        return this;
    }

    public FeedbackSubmissionSpecificationBuilder filterByMentor(Long mentorId){
        if(mentorId == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<FeedbackSubmission, Class> feedbackSubmissionClassJoin = root.join(FeedbackSubmission_.CLAZZ);
            Join<Class, Course> classCourseJoin = feedbackSubmissionClassJoin.join(Class_.COURSE);
            Join<Course, User> courseUserJoin = classCourseJoin.join(Course_.CREATOR);
            return criteriaBuilder.equal(courseUserJoin.get(User_.ID), mentorId);
        });
        return this;
    }

    public FeedbackSubmissionSpecificationBuilder filterByRate(Integer rate, Boolean isCourse){
        if(rate == null || isCourse == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(isCourse ? root.get(FeedbackSubmission_.COURSE_RATE) : root.get(FeedbackSubmission_.MENTOR_RATE), rate));
        return this;
    }
    public Specification<FeedbackSubmission> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }
    public Specification<FeedbackSubmission> all(){return Specification.where(null);}
}
