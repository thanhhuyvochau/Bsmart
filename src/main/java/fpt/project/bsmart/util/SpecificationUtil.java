package fpt.project.bsmart.util;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

public class SpecificationUtil {
    private SpecificationUtil() {
    }

    @SafeVarargs
    public static Expression<String> concat(CriteriaBuilder criteriaBuilder, String delimiter, Expression<String>... expressions) {
        Expression<String> result = null;
        for (int i = 0; i < expressions.length; i++) {
            final boolean first = i == 0, last = i == (expressions.length - 1);
            Expression<String> expression = expressions[i];
            expression = criteriaBuilder.coalesce(expression, Strings.EMPTY);
            if (first && last) {
                result = expression;
            } else if (first) {
                result = criteriaBuilder.concat(expression, delimiter);
            } else {
                result = criteriaBuilder.concat(result, expression);
                if (!last) {
                    result = criteriaBuilder.concat(result, delimiter);
                }
            }
        }
        return result;
    }
}
