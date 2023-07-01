package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubCourseSpecificationBuilder {

    public static SubCourseSpecificationBuilder specifications() {
        return new SubCourseSpecificationBuilder();
    }

//    private final List<Specification<SubCourse>> specifications = new ArrayList<>();


//    public SubCourseSpecificationBuilder queryLike(String q) {
//        if (q == null || q.trim().isEmpty()) {
//            return this;
//        }
//
//        specifications.add((root, query, criteriaBuilder) -> {
//
//            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ");
//            String search = q.replaceAll("\\s\\s+", " ").trim();
//            return criteriaBuilder.like(stringExpression, '%' + search + '%');
//        });
//        return this;
//
//    }

//    public SubCourseSpecificationBuilder queryBySubCourseStatus(ECourseStatus status) {
//        if (status == null) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get(SubCourse_.STATUS), status);
//        });
//        return this;
//    }
//
//    public SubCourseSpecificationBuilder queryBySubCourseType(ETypeLearn type) {
//        if (type == null) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get(SubCourse_.TYPE_LEARN), type);
//        });
//        return this;
//    }


//    public SubCourseSpecificationBuilder queryBySubjectId(List<Long> subjectId) {
//        if (subjectId == null) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            Join<SubCourse, Course> courseJoin = root.join(SubCourse_.COURSE, JoinType.INNER);
//            Join<Course, Subject> courseSubjectJoin = courseJoin.join(Course_.SUBJECT, JoinType.INNER);
//            return criteriaBuilder.and(courseSubjectJoin.get(Subject_.ID).in(subjectId));
//        });
//        return this;
//    }

//    public SubCourseSpecificationBuilder queryByCategoryId(List<Long> categoryId) {
//        if (categoryId == null) {
//            return this;
//        }
//        specifications.add((root, query, criteriaBuilder) -> {
//            Join<SubCourse, Course> courseJoin = root.join(SubCourse_.COURSE, JoinType.INNER);
//            Join<Course, Subject> courseSubjectJoin = courseJoin.join(Course_.SUBJECT, JoinType.INNER);
//            Join<Subject, Category> subjectCategoryJoin = courseSubjectJoin.join(Subject_.CATEGORIES, JoinType.INNER);
//
//            return criteriaBuilder.and(subjectCategoryJoin.get(Category_.ID).in(categoryId));
//        });
//        return this;
//    }

//    public Specification<SubCourse> build() {
//        return specifications.stream()
//                .filter(Objects::nonNull)
//                .reduce(all(), Specification::and);
//    }

//    private Specification<SubCourse> all() {
//        return Specification.where(null);
//    }
}
