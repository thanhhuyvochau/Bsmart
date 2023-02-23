package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ERequestStatus;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestFormSpecificationBuilder {

    public static RequestFormSpecificationBuilder specification() {
        return new RequestFormSpecificationBuilder();
    }

    private final List<Specification<Request>> specifications = new ArrayList<>();


    public RequestFormSpecificationBuilder queryByStudent(Long studentId) {
        if (studentId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> root.get(Request_.ACCOUNT).in(studentId));
        return this;
    }


    public RequestFormSpecificationBuilder queryByStatus(ERequestStatus statuses) {
        if (statuses == null) {
            return this;
        }
        if (statuses.equals(ERequestStatus.ALL)) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> root.get(Request_.STATUS).in(statuses));
        return this;
    }


    public RequestFormSpecificationBuilder queryLike(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> title = root.get(Request_.title);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", title);
            String search = q.replaceAll("\\s\\s+", " ").trim();
            return criteriaBuilder.like(stringExpression, '%' + search + '%');
        });

        return this;

    }


    public RequestFormSpecificationBuilder queryByDate(Instant dateFrom, Instant dateTo) {
        if (dateFrom == null) {
            return this;
        }
        Instant dateToPlus = dateTo.plus(1, ChronoUnit.DAYS);
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Request_.lastModified), dateFrom));
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Request_.lastModified), dateToPlus));
        return this;
    }


    public Specification<Request> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Request> all() {
        return Specification.where(null);
    }


}