package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITeacherService {
    List<Account> getTeacher();

    ApiPage<AccountDetailResponse> getAllTeacher(Pageable pageable);

}
