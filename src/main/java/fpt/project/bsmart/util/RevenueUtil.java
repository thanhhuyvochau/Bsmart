package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.response.SalaryEstimatesResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RevenueUtil {

    public static  List<SalaryEstimatesResponse> payPriceTeacherReceived(BigDecimal priceEachStudent, Long numberStudent , Long numberMonth)  {

        List<SalaryEstimatesResponse> salaryEstimatesResponseList = new ArrayList<>( );
        SalaryEstimatesResponse salaryEstimatesResponse = new SalaryEstimatesResponse();

        BigDecimal payPriceToTeacherForOneMonth = BigDecimal.ZERO ;

        BigDecimal payPriceToTeacherForManyMonth = BigDecimal.ZERO ;

        BigDecimal payPriceToSystemForOneMonth = BigDecimal.ZERO ;
        BigDecimal payPriceToSystemForManyMonth = BigDecimal.ZERO ;

        //estimate salary for teacher
        BigDecimal multiply =priceEachStudent.multiply(BigDecimal.valueOf(numberStudent));
        payPriceToTeacherForOneMonth = multiply.multiply(BigDecimal.valueOf(70)).divide(BigDecimal.valueOf(100) );
        salaryEstimatesResponse.setEstimatesSalaryOneMonthForTeacher(payPriceToTeacherForOneMonth);


        payPriceToTeacherForManyMonth = payPriceToTeacherForOneMonth.multiply(BigDecimal.valueOf(numberMonth)) ;
        salaryEstimatesResponse.setEstimatesSalaryManyMonthForTeacher(payPriceToTeacherForManyMonth);

        //estimate salary for vuondau

        payPriceToSystemForOneMonth = multiply.multiply(BigDecimal.valueOf(30)).divide(BigDecimal.valueOf(100));
        salaryEstimatesResponse.setEstimatesSalaryOneMonthForSystem(payPriceToSystemForOneMonth);


        payPriceToSystemForManyMonth = payPriceToSystemForOneMonth.multiply(BigDecimal.valueOf(numberMonth)) ;
        salaryEstimatesResponse.setEstimatesSalaryManyMonthForSystem(payPriceToSystemForManyMonth);

        salaryEstimatesResponseList.add(salaryEstimatesResponse) ;

        return salaryEstimatesResponseList ;

    }







}
