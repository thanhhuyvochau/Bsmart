package fpt.project.bsmart.entity.response;



import java.math.BigDecimal;


public class SalaryEstimatesResponse {


    private BigDecimal estimatesSalaryOneMonthForTeacher;


    private BigDecimal estimatesSalaryManyMonthForTeacher;

    private BigDecimal estimatesSalaryOneMonthForSystem;

    private BigDecimal estimatesSalaryManyMonthForSystem;


    public BigDecimal getEstimatesSalaryOneMonthForTeacher() {
        return estimatesSalaryOneMonthForTeacher;
    }

    public void setEstimatesSalaryOneMonthForTeacher(BigDecimal estimatesSalaryOneMonthForTeacher) {
        this.estimatesSalaryOneMonthForTeacher = estimatesSalaryOneMonthForTeacher;
    }

    public BigDecimal getEstimatesSalaryManyMonthForTeacher() {
        return estimatesSalaryManyMonthForTeacher;
    }

    public void setEstimatesSalaryManyMonthForTeacher(BigDecimal estimatesSalaryManyMonthForTeacher) {
        this.estimatesSalaryManyMonthForTeacher = estimatesSalaryManyMonthForTeacher;
    }

    public BigDecimal getEstimatesSalaryOneMonthForSystem() {
        return estimatesSalaryOneMonthForSystem;
    }

    public void setEstimatesSalaryOneMonthForSystem(BigDecimal estimatesSalaryOneMonthForSystem) {
        this.estimatesSalaryOneMonthForSystem = estimatesSalaryOneMonthForSystem;
    }

    public BigDecimal getEstimatesSalaryManyMonthForSystem() {
        return estimatesSalaryManyMonthForSystem;
    }

    public void setEstimatesSalaryManyMonthForSystem(BigDecimal estimatesSalaryManyMonthForSystem) {
        this.estimatesSalaryManyMonthForSystem = estimatesSalaryManyMonthForSystem;
    }
}
