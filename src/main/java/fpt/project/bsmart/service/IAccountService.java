package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.StudentResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

public interface IAccountService {
    Optional<User> findByUsername(String username);

    User saveAccount(User user);

    ApiPage<AccountResponse> getAccounts(Pageable pageable);

    ApiPage<AccountResponse> getTeacherAccounts(Pageable pageable);

    ApiPage<AccountResponse> getStudentAccounts(Pageable pageable);

    StudentResponse createStudentAccount(StudentRequest studentRequest);

    Boolean uploadAvatar(long id, UploadAvatarRequest uploadAvatarRequest) throws IOException;


    AccountResponse editProfile(long id, AccountEditRequest accountEditRequest);

    AccountResponse getAccountById(long id);

    AccountResponse editStudentProfile(long id, AccountEditRequest accountEditRequest);

    Boolean banAndUbBanAccount(long id);

    ApiPage<AccountResponse> searchAccount(AccountSearchRequest query, Pageable pageable);

    AccountResponse updateRoleAndActiveAccount(long id, AccountEditRequest accountEditRequest);

    AccountResponse approveTeacherAccount(long id);

    AccountResponse getSelfAccount();

    AccountResponse createManagerOrAccountant(CreateAccountRequest request);


}
