package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.Subject_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuggestSubjectSpecificationBuilder {

    public static SuggestSubjectSpecificationBuilder specification() {
        return new SuggestSubjectSpecificationBuilder();
    }

    private final List<Specification<Subject>> specifications = new ArrayList<>();


    public SuggestSubjectSpecificationBuilder querySubjectCode(String subjectCode) {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> courseCode = root.get(Subject_.NAME);


            return criteriaBuilder.like(courseCode, subjectCode);
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