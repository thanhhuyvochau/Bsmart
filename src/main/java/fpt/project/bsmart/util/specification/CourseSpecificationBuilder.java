package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

public class CourseSpecificationBuilder {

    public static CourseSpecificationBuilder specifications() {
        return new CourseSpecificationBuilder();
    }

    private final List<Specification<Course>> specifications = new ArrayList<>();


    public CourseSpecificationBuilder queryLike(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> courseName = root.get(Course_.name);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", courseName);
            String search = q.replaceAll("\\s\\s+", " ").trim();
            return criteriaBuilder.like(stringExpression, '%' + search + '%');
        });
        return this;

    }

    public CourseSpecificationBuilder queryByCourseStatus(ECourseStatus status) {
        if (status == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(Course_.STATUS), status);
        });
        return this;
    }


    public CourseSpecificationBuilder queryBySubjectId(Long subjectId) {
        if (subjectId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {

            return criteriaBuilder.and(root.get(Course_.SUBJECT).in(subjectId));
        });
        return this;
    }

    public CourseSpecificationBuilder queryByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Course, Subject> courseSubjectJoin = root.join(Course_.SUBJECT, JoinType.INNER);
            Join<Subject, Category> categoryJoin = courseSubjectJoin.join(Subject_.CATEGORY);
            Path<Object> objectPath = categoryJoin.get(Category_.ID);
            return criteriaBuilder.and(objectPath.in(categoryId));
        });
        return this;
    }

    public Specification<Course> build() {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Course> all() {
        return Specification.where(null);
    }
}
