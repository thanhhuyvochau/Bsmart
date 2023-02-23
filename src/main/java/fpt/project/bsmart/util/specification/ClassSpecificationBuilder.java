package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.EClassLevel;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.common.EClassType;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassSpecificationBuilder {

    public static ClassSpecificationBuilder specification() {
        return new ClassSpecificationBuilder();
    }

    private final List<Specification<Class>> specifications = new ArrayList<>();

    public ClassSpecificationBuilder queryByClassType(EClassType classTypes) {
        if (classTypes == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> root.get(Class_.CLASS_TYPE).in(classTypes));

        return this;

    }

    public ClassSpecificationBuilder queryBySubject(Long subjectId) {
        if (subjectId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
        {
            Path<Course> objectCourse = root.get(Class_.COURSE);
            Path<Subject> subjectPath = objectCourse.get(Course_.subject);

            return criteriaBuilder.or((subjectPath.get(Subject_.ID)).in(subjectId));
        });
        return this;
    }

    public ClassSpecificationBuilder queryByStudent(Account student) {
        if (student == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
        {
            Join<StudentClass, Class> studentClassJoin = root.join(Class_.STUDENT_CLASSES);
            return criteriaBuilder.or((studentClassJoin.get(StudentClass_.ACCOUNT)).in(student));
        });
        return this;
    }


    public ClassSpecificationBuilder queryByClassStatus(EClassStatus statuses) {
        if (statuses == null) {
            return this;
        }
        if (statuses.equals(EClassStatus.All)) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> root.get(Class_.STATUS).in(statuses));
        return this;
    }

    public ClassSpecificationBuilder queryByDate(Instant dateFrom, Instant dateTo) {
        if (dateFrom == null && dateTo == null) {
            return this;
        }


        Instant dateToPlus = dateTo.plus(1, ChronoUnit.DAYS);
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Class_.START_DATE), dateFrom));
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Class_.END_DATE), dateToPlus));
        return this;
    }

    public ClassSpecificationBuilder queryByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            return this;
        }
        if (maxPrice.compareTo(minPrice) < 0) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Class_.UNIT_PRICE), minPrice, maxPrice));
        return this;
    }



    public ClassSpecificationBuilder query(String q) {
        if (q == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> classname = root.get(Class_.name);
            Expression<String> classCode = root.get(Class_.code);
            Join<Account, Class> accountClassJoin = root.join(Class_.ACCOUNT);
//            Join<AccountDetail, Account> accountDetailAccountJoin = accountClassJoin.join(Account_.ACCOUNT_DETAIL);
//            Expression<String> teacherName = accountDetailAccountJoin.get(AccountDetail_.firstName.toString().concat(AccountDetail_.lastName.toString()));
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", classname, classCode);
            return criteriaBuilder.like(stringExpression, '%' + q + '%');

        });
        return this;
    }

    public Specification<Class> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Class> all() {
        return Specification.where(null);
    }

    public ClassSpecificationBuilder queryLevelClass(EClassLevel level) {
        if (level == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Class_.CLASS_LEVEL), level));
        return this;
    }

    public ClassSpecificationBuilder queryTeachersClass(List<Account> teachers) {
        if (teachers == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(root.get(Class_.account).in(teachers)))
        );
        return this;
    }

    public ClassSpecificationBuilder queryTeacherClass(Long teacherId) {
        if (teacherId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
        {
            Join<Account, Class> accountClassJoin = root.join(Class_.ACCOUNT);
            return criteriaBuilder.or((accountClassJoin.get(Account_.ID)).in(teacherId));
        });
        return this;
    }

    public ClassSpecificationBuilder querySubjectClass(List<Subject> subjects) {
        if (subjects == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Path<Course> coursePath = root.get(Class_.course);
            CriteriaBuilder.In<Subject> inClause = criteriaBuilder.in(coursePath.get(Course_.subject));
            for (Subject subject : subjects) {
                inClause.value(subject);
            }
            return inClause;
        });


        return this;
    }

    public ClassSpecificationBuilder isActive(Boolean isActive) {
        if (isActive == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get(Class_.IS_ACTIVE), isActive))
        );
        return this;
    }
}