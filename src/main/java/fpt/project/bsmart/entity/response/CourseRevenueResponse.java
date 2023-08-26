package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;

public class CourseRevenueResponse {
    private Long courseId;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal promotion = BigDecimal.ZERO;
    private BigDecimal revenue = BigDecimal.ZERO;
}
