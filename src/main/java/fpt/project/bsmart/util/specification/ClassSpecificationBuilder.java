package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassSpecificationBuilder {
    public static ClassSpecificationBuilder classSpecificationBuilder() {
        return new ClassSpecificationBuilder();
    }

    private List<Specification<Class>> specifications = new ArrayList<>();

//    public ClassSpecificationBuilder searchByMentorName(String name) {
//        if (StringUtil.isNullOrEmpty(name)) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            Join<Class, SubCourse> classSubCourseJoin = root.join(SubCourse_.MENTOR, JoinType.LEFT);
//            return criteriaBuilder.like(classSubCourseJoin.get(User_.FULL_NAME), "%" + name + "%");
//        });
//        return this;
//    }

//    public ClassSpecificationBuilder searchBySubCourseName(String name) {
//        if (StringUtil.isNullOrEmpty(name)) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            Expression<String> expression = root.get(SubCourse_.TITLE);
//            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", expression);
//            String search = name.replaceAll("\\s\\s", " ").trim();
//            return criteriaBuilder.like(stringExpression, "%" + search + "%");
//        });
//        return this;
//    }

    public ClassSpecificationBuilder filterByStartDay(Instant startDate) {
        if (startDate == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<Instant> expression = root.get(Class_.START_DATE);
            return criteriaBuilder.greaterThanOrEqualTo(expression, startDate);
        });
        return this;
    }

    public ClassSpecificationBuilder filterByEndDate(Instant endDate) {
        if (endDate == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<Instant> expression = root.get(Class_.END_DATE);
            return criteriaBuilder.lessThanOrEqualTo(expression, endDate);
        });
        return this;
    }

    public ClassSpecificationBuilder searchByCourseName(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Path<Course> coursePath = root.get(Class_.COURSE);
            return criteriaBuilder.like(coursePath.get(Course_.NAME), "%" + name + "%");
        });
        return this;
    }

    public ClassSpecificationBuilder byMentor(User mentor) {
        if (mentor == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Path<Course> coursePath = root.get(Class_.COURSE);
            return criteriaBuilder.equal(coursePath.get(Course_.CREATOR), mentor);
        });
        return this;
    }

    public ClassSpecificationBuilder byStudent(User student) {
        if (student == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Class_.STUDENT_CLASSES).get(StudentClass_.STUDENT)).value(student));
        return this;
    }

    public ClassSpecificationBuilder filterByStatus(ECourseStatus status) {
        if (status == null) {
            return this;
        }
        switch (status) {
            case STARTING:
                return getStartingClass();
            case ENDED:
                return getEndClass();
            default:
                return this;
        }
    }

    public ClassSpecificationBuilder getEndClass() {
        Instant now = Instant.now();
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Class_.END_DATE), now));
        return this;
    }

    public ClassSpecificationBuilder getStartingClass() {
        Instant now = Instant.now();
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(Class_.END_DATE), now),
                criteriaBuilder.lessThanOrEqualTo(root.get(Class_.START_DATE), now)));
        return this;
    }

    public Specification<Class> build() {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Class> all() {
        return Specification.where(null);
    }
}
