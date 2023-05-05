package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.util.SpecificationUtil;
import fpt.project.bsmart.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassFeedbackSpecificationBuilder {
    public static ClassFeedbackSpecificationBuilder classFeedbackSpecificationBuilder () {return new ClassFeedbackSpecificationBuilder();}
    private List<Specification<Class>> specifications = new ArrayList<>();

    public ClassFeedbackSpecificationBuilder searchByMentorName(String name){
        if(StringUtil.isNullOrEmpty(name)){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Class, SubCourse> classSubCourseJoin = root.join(SubCourse_.MENTOR, JoinType.LEFT);
            return criteriaBuilder.like(classSubCourseJoin.get(User_.FULL_NAME), "%" + name + "%");
        });
        return this;
    }

    public ClassFeedbackSpecificationBuilder searchBySubCourseName(String name){
        if(StringUtil.isNullOrEmpty(name)){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> expression = root.get(SubCourse_.TITLE);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", expression);
            String search = name.replaceAll("\\s\\s", " ").trim();
            return criteriaBuilder.like(stringExpression, "%" + search + "%");
        });
        return this;
    }
    public ClassFeedbackSpecificationBuilder filterByStartDay(Instant startDate){
        if(startDate == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<Instant> expression = root.get(Class_.START_DATE);
            return criteriaBuilder.greaterThanOrEqualTo(expression, startDate);
        });
        return this;
    }

    public ClassFeedbackSpecificationBuilder filterByEndDate(Instant endDate){
        if(endDate == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<Instant> expression = root.get(Class_.END_DATE);
            return criteriaBuilder.lessThanOrEqualTo(expression, endDate);
        });
        return this;
    }

    public Specification build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }
    private Specification<Class> all(){
        return Specification.where(null);
    }
}
