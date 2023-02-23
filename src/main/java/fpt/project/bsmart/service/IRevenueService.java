package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.RevenueSearchRequest;
import fpt.project.bsmart.entity.response.RevenueClassResponse;
import fpt.project.bsmart.entity.response.SalaryEstimatesResponse;

import java.math.BigDecimal;
import java.util.List;

public interface IRevenueService {


    List<SalaryEstimatesResponse> salaryEstimatesTeachers(BigDecimal priceEachStudent, Long numberStudent, Long numberMonth);

    BigDecimal RevenueClass(Long classId);

    List<RevenueClassResponse> RevenueAllClass();

    List<RevenueClassResponse> searchRevenue(RevenueSearchRequest query);

    List<RevenueClassResponse> studentGetTuitionFee(RevenueSearchRequest query);
}
