package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseSpecificationBuilder {

    public static CourseSpecificationBuilder specification() {
        return new CourseSpecificationBuilder();
    }

    private final List<Specification<Course>> specifications = new ArrayList<>();


    public CourseSpecificationBuilder queryLike(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> courseCode = root.get(Course_.CODE);
            Expression<String> courseName = root.get(Course_.NAME);
            Join<Course, TeacherCourse> teacherCourseJoin = root.join(Course_.teacherCourses, JoinType.INNER);
            Join<TeacherCourse, Account> teacherCourseAccountJoin = teacherCourseJoin.join(TeacherCourse_.ACCOUNT, JoinType.INNER);
//            Expression<String> lastName = teacherCourseAccountJoin.get(Account_.LAST_NAME);
//            Expression<String> firstName = teacherCourseAccountJoin.get(Account_.FIRST_NAME);
            Join<Course, Subject> courseSubjectJoin = root.join(Course_.subject, JoinType.INNER);
            Expression<String> subject = courseSubjectJoin.get(Subject_.name);

            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " " ,courseCode, courseName, subject);
            return criteriaBuilder.like(stringExpression, '%' + q + '%');
        });


        return this;
    }

    public Specification<Course> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Course> all() {
        return Specification.where(null);
    }


}