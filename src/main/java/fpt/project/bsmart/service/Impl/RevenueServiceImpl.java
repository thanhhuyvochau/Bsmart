package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.request.RevenueSearchRequest;
import fpt.project.bsmart.entity.response.RevenueClassResponse;
import fpt.project.bsmart.entity.response.SalaryEstimatesResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IRevenueService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.RevenueSpecificationBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RevenueServiceImpl implements IRevenueService {

    private final ClassRepository classRepository;

    private final TransactionRepository transactionRepository;

    private final SecurityUtil securityUtil;

    public RevenueServiceImpl(ClassRepository classRepository, TransactionRepository transactionRepository, SecurityUtil securityUtil) {
        this.classRepository = classRepository;
        this.transactionRepository = transactionRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<SalaryEstimatesResponse> salaryEstimatesTeachers(BigDecimal priceEachStudent, Long numberStudent, Long numberMonth) {
        return RevenueUtil.payPriceTeacherReceived(priceEachStudent, numberStudent, numberMonth);
    }

    @Override
    public BigDecimal RevenueClass(Long classId) {

        Class aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));
        List<Transaction> allByPaymentClass = transactionRepository.findAllByPaymentClass(aClass);
        Long totalMoney = 0L;
        for (Transaction transaction : allByPaymentClass) {
            if (transaction.getSuccess() != null) {
                if (transaction.getSuccess()) {
                    Long l = transaction.getAmount().longValue();
                    totalMoney += l;
                }
            }

        }
        return BigDecimal.valueOf(totalMoney);

    }

    @Override
    public List<RevenueClassResponse> RevenueAllClass() {

        List<Transaction> allByPaymentClass = transactionRepository.findAll();
        List<Class> allClass = allByPaymentClass.stream().map(Transaction::getPaymentClass).distinct().collect(Collectors.toList());

        List<RevenueClassResponse> revenueClassResponseList = new ArrayList<>();

        allClass.forEach(aClass -> {
            RevenueClassResponse revenueClassResponse = new RevenueClassResponse();
            revenueClassResponse.setClassId(aClass.getId());
            List<Transaction> transactions = transactionRepository.findAllByPaymentClass(aClass);
            Long totalMoney = 0L;
            for (Transaction transaction : transactions) {
                if (transaction.getSuccess() != null) {
                    if (transaction.getSuccess()) {
                        Long l = transaction.getAmount().longValue();
                        totalMoney += l;
                    }
                }
                revenueClassResponse.setRevenue(totalMoney);
            }

            revenueClassResponseList.add(revenueClassResponse);
        });

        return revenueClassResponseList;
    }

    @Override
    public List<RevenueClassResponse> searchRevenue(RevenueSearchRequest query) {
//        List<Class> allById = new ArrayList<>() ;
//        if (query.getClassIds()!= null){
//            allById  = classRepository.findAllById(query.getClassIds());
//
//        }
        RevenueSpecificationBuilder builder = RevenueSpecificationBuilder.specification()

                .queryByClasses(query.getClassIds())
                .studentPayDate(query.getDateFrom(),query.getDateTo())
                .queryByTeacherIds(query.getTeacherIds()) ;
//                .queryByTeacherIds(query.getTeacherIds());
        List<Transaction> transactionPage = transactionRepository.findAll(builder.build());
        List<Class> allClass = transactionPage.stream().map(Transaction::getPaymentClass).distinct().collect(Collectors.toList());

        List<RevenueClassResponse> revenueClassResponseList = new ArrayList<>();
        for (Class aClass : allClass) {
            RevenueClassResponse revenueClassResponse = new RevenueClassResponse();
            revenueClassResponse.setClassId(aClass.getId());
            List<Transaction> transactions = transactionRepository.findAllByPaymentClass(aClass);
            Long totalMoney = 0L;
            for (Transaction transaction : transactions) {

                    if (transaction.getSuccess() != null) {
                        if (transaction.getAmount() != null && transaction.getSuccess() && transaction.getTransactionNo() != null) {
                            Long l = transaction.getAmount().longValue();
                            totalMoney += l;
                        }
                    }
                revenueClassResponse.setRevenue(totalMoney);
            }
            revenueClassResponseList.add(revenueClassResponse);
        }

        return revenueClassResponseList;


    }

    @Override
    public List<RevenueClassResponse> studentGetTuitionFee(RevenueSearchRequest query) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        List<Long> ids = new ArrayList<>();


        List<RevenueClassResponse> revenueClassResponseList = new ArrayList<>();

        RevenueSpecificationBuilder builder = RevenueSpecificationBuilder.specification()
                .studentPayDate(query.getDateFrom(), query.getDateTo())
                .queryByClasses(query.getClassIds());

        if (account.getRole().getCode().equals(EAccountRole.STUDENT)) {
            ids.add(account.getId());
            builder.queryByStudentsIn(ids);
        }
        if (account.getRole().getCode().equals(EAccountRole.TEACHER)) {
            ids.add(account.getId());
            builder.queryByTeacherIds(ids);
        }

        List<Transaction> transactionPage = transactionRepository.findAll(builder.build());

        for (Transaction transaction : transactionPage) {
            if (transaction.getSuccess() != null) {
                if (transaction.getAmount() != null && transaction.getSuccess() && transaction.getTransactionNo() != null) {
                    RevenueClassResponse revenueClassResponse = new RevenueClassResponse();
                    revenueClassResponse.setClassId(transaction.getPaymentClass().getId());
                    revenueClassResponse.setRevenue(transaction.getAmount().longValue());
                    revenueClassResponse.setOrderInfo(transaction.getOrderInfo());
                    revenueClassResponse.setPayDate(transaction.getPayDate());
                    revenueClassResponse.setSuccess(transaction.getSuccess());
                    revenueClassResponse.setTransactionNo(transaction.getTransactionNo());
                    revenueClassResponseList.add(revenueClassResponse);
                }
            }
        }
        return revenueClassResponseList;
    }
}
