package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RevenueSpecificationBuilder {

    public static RevenueSpecificationBuilder specification() {
        return new RevenueSpecificationBuilder();
    }

    private final List<Specification<Transaction>> specifications = new ArrayList<>();


    public RevenueSpecificationBuilder queryByClasses(List<Long> classes) {

        if (classes == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Class> classJoin = root.join(Transaction_.PAYMENT_CLASS, JoinType.INNER);
            return criteriaBuilder.or(criteriaBuilder.or(classJoin.get(Class_.ID).in(classes))

            );
        });
        return this;

    }

    public RevenueSpecificationBuilder queryByStudentsIn(List<Long> studentIds) {
        if (studentIds == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(root.get(Transaction_.ACCOUNT).in(studentIds));
        });

        return this;
    }

    public RevenueSpecificationBuilder queryByTeacherIds(List<Long> teacherIds) {
        if (teacherIds == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Class> classJoin = root.join(Transaction_.PAYMENT_CLASS, JoinType.INNER);
            return criteriaBuilder.or(criteriaBuilder.or(classJoin.get(Class_.ACCOUNT).in(teacherIds))

            );
        });

        return this;
    }

    public RevenueSpecificationBuilder studentPayDate(Instant dateFrom, Instant dateTo) {
        if (dateFrom == null && dateTo == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction_.PAY_DATE), dateFrom));
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Transaction_.PAY_DATE), dateTo));
        return this;
    }

    public RevenueSpecificationBuilder date(Instant dateFrom, Instant dateTo) {
        if (dateFrom == null && dateTo == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Class> classJoin = root.join(Transaction_.PAYMENT_CLASS, JoinType.INNER);

            return criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(classJoin.get(Class_.startDate), dateFrom),
                    criteriaBuilder.lessThanOrEqualTo(classJoin.get(Class_.endDate), dateTo));

        });

        return this;
    }

    public Specification<Transaction> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Transaction> all() {
        return Specification.where(null);
    }

}