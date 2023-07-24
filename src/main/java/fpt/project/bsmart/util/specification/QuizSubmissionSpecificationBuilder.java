package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.constant.EComparison;
import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizSubmissionSpecificationBuilder {
    public static QuizSubmissionSpecificationBuilder quizSubmissionSpecificationBuilder(){
        return new QuizSubmissionSpecificationBuilder();
    }

    private List<Specification> specifications = new ArrayList<>();

    public QuizSubmissionSpecificationBuilder queryByQuiz(Long id){
        specifications.add((root, query, criteriaBuilder) -> {
            Join<QuizSubmittion, Quiz> join = root.join(QuizSubmittion_.QUIZ);
            return criteriaBuilder.equal(join.get(Quiz_.ID), id);
        });
        return this;
    }

    public QuizSubmissionSpecificationBuilder queryByStatus(QuizSubmittionStatus status){
        if(status == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(QuizSubmittion_.STATUS), status));
        return this;
    }

    public QuizSubmissionSpecificationBuilder queryByPoint(EComparison comparison, Float point){
        if(comparison == null){
            return this;
        }
        if(point < 0 || point > 10){
            return this;
        }
        switch (comparison){
            case LESS_THAN:
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(QuizSubmittion_.POINT), point));
                break;
            case LESS_THAN_OR_EQUAL_TO:
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(QuizSubmittion_.POINT), point));
                break;
            case EQUAL_TO:
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(QuizSubmittion_.POINT), point));
                break;
            case GREATER_THAN:
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(QuizSubmittion_.POINT), point));
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(QuizSubmittion_.POINT), point));
                break;
            default:
                return this;
        }
        return this;
    }

    public QuizSubmissionSpecificationBuilder queryByClass(Long classId){
        if(classId == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<QuizSubmittion, Quiz> join = root.join(QuizSubmittion_.QUIZ);
            Join<Quiz, Activity> quizActivityJoin = join.join(Quiz_.ACTIVITY);
            Join<Activity, ActivityAuthorize> activityAuthorizeJoin = quizActivityJoin.join(Activity_.ACTIVITY_AUTHORIZES);
            Join<ActivityAuthorize, Class> activityAuthorizeClassJoin = activityAuthorizeJoin.join(ActivityAuthorize_.AUTHORIZE_CLASS);
            return criteriaBuilder.equal(activityAuthorizeClassJoin.get(Class_.ID), classId);
        });
        return this;
    }

    public QuizSubmissionSpecificationBuilder isAfter(Instant date){
        if(date == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(QuizSubmittion_.CREATED), date));
        return this;
    }

    public QuizSubmissionSpecificationBuilder isBefore(Instant date){
        if(date == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(QuizSubmittion_.CREATED), date));
        return this;
    }

    public Specification<QuizSubmittion> all(){
        return Specification.where(null);
    }

    public Specification<QuizSubmittion> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }
}
