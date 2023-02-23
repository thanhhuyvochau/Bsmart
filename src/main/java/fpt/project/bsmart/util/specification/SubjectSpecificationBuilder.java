package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.Subject_;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubjectSpecificationBuilder {

    public static SubjectSpecificationBuilder specification() {
        return new SubjectSpecificationBuilder();
    }

    private final List<Specification<Subject>> specifications = new ArrayList<>();


    public SubjectSpecificationBuilder queryLike(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> courseCode = root.get(Subject_.CODE);
            Expression<String> courseName = root.get(Subject_.NAME);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ", courseCode, courseName);
            return criteriaBuilder.like(stringExpression, '%' + q + '%');
        });


        return this;
    }

    public Specification<Subject> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Subject> all() {
        return Specification.where(null);
    }


}