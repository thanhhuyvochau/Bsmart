package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.AccountTeacherResponse;
import fpt.project.bsmart.entity.response.StudentResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

public interface IAccountService {
    Optional<Account> findByUsername(String username);

    Account saveAccount(Account account);

    AccountTeacherResponse createTeacherAccount(AccountExistedTeacherRequest accountRequest);

    ApiPage<AccountResponse> getAccounts(Pageable pageable);

    ApiPage<AccountResponse> getTeacherAccounts(Pageable pageable);

    ApiPage<AccountResponse> getStudentAccounts(Pageable pageable);

    StudentResponse createStudentAccount(StudentRequest studentRequest);

    Boolean uploadAvatar(long id, UploadAvatarRequest uploadAvatarRequest) throws IOException;


    AccountResponse editProfile(long id, AccountEditRequest accountEditRequest);

    AccountResponse getAccountById(long id);

    AccountResponse editTeacherProfile(long id, AccountEditRequest accountEditRequest);

    AccountResponse editStudentProfile(long id, AccountEditRequest accountEditRequest);

    Boolean banAndUbBanAccount(long id);

    ApiPage<AccountResponse> searchAccount(AccountSearchRequest query, Pageable pageable);

    AccountResponse updateRoleAndActiveAccount(long id,AccountEditRequest accountEditRequest);
    AccountResponse approveTeacherAccount(long id);

    AccountResponse getSelfAccount();

    ApiPage<AccountDetailResponse> getAllInfoTeacher(Pageable pageable);
    AccountResponse createManagerOrAccountant(CreateAccountRequest request);

    ApiPage<AccountResponse> getStaffAccounts(Pageable pageable);


}
