package fpt.project.bsmart.util.specification;


import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Account_;
import fpt.project.bsmart.util.SpecificationUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountSpecificationBuilder {

    public static AccountSpecificationBuilder specification() {
        return new AccountSpecificationBuilder();
    }

    private final List<Specification<Account>> specifications = new ArrayList<>();


    public AccountSpecificationBuilder queryLike(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this;
        }

        specifications.add((root, query, criteriaBuilder) -> {
            Expression<String> username = root.get(Account_.username);
            Expression<String> stringExpression = SpecificationUtil.concat(criteriaBuilder, " ",  username);
            return criteriaBuilder.like(stringExpression, '%' + q + '%');
        });
        return this;
    }

    public Specification<Account> build() {
        return specifications.stream().filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Account> all() {
        return Specification.where(null);
    }


}