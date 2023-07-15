package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Instant;
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
            Expression<String> courseCode = root.get(Course_.code);
            Expression<String> courseName = root.get(Course_.name);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", courseName, courseCode);
            String search = q.replaceAll("\\s\\s+", " ").trim();
            return criteriaBuilder.like(stringExpression, '%' + search + '%');
        });
        return this;

    }


    public CourseSpecificationBuilder queryByCourseStatus(ECourseStatus status) {
        if (status == null) {
            return this;
        }
        if (status.equals(ECourseStatus.ALL)){
            return this ;
        }
        specifications.add((root, query, criteriaBuilder) -> {

            return criteriaBuilder.equal(root.get(Course_.STATUS), status);
        });
        return this;
    }


    public CourseSpecificationBuilder queryBySubjectId(List<Long> subjectId) {
        if (subjectId == null || subjectId.isEmpty()) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Path<Subject> objectPath = root.get(Course_.SUBJECT);
            return criteriaBuilder.and(objectPath.get(Subject_.ID).in(subjectId));
        });
        return this;
    }

    public CourseSpecificationBuilder queryByCreatorId(Long creatorId) {
        if (creatorId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Path<Subject> objectPath = root.get(Course_.CREATOR);
            return criteriaBuilder.and(objectPath.get(User_.ID).in(creatorId));
        });
        return this;
    }

    public CourseSpecificationBuilder queryByCategoryId(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Course, Subject> courseSubjectJoin = root.join(Course_.SUBJECT, JoinType.INNER);
            Join<Subject, Category> categoryJoin = courseSubjectJoin.join(Subject_.CATEGORIES);
            Path<Object> objectPath = categoryJoin.get(Category_.ID);
            return criteriaBuilder.and(objectPath.in(categoryIds));
        });
        return this;
    }

    public CourseSpecificationBuilder queryStudentCurrentCourse(Long studentId){
        Instant now = Instant.now();
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Course, Class> courseClassJoin = root.join(Course_.CLASSES);
            Join<Class, StudentClass> studentClassJoin = courseClassJoin.join(Class_.STUDENT_CLASSES);
            Predicate userPredicate = criteriaBuilder.equal(studentClassJoin.get(StudentClass_.STUDENT), studentId);
            Predicate startDatePredicate = criteriaBuilder.lessThanOrEqualTo(courseClassJoin.get(Class_.START_DATE), now);
            Predicate endDatePredicate = criteriaBuilder.greaterThanOrEqualTo(courseClassJoin.get(Class_.END_DATE), now);
            return criteriaBuilder.and(userPredicate, startDatePredicate, endDatePredicate);
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
