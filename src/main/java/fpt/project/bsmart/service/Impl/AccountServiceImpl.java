package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.dto.RoleDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.StudentResponse;

import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.service.IAccountService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import fpt.project.bsmart.util.keycloak.KeycloakRoleUtil;
import fpt.project.bsmart.util.keycloak.KeycloakUserUtil;
import io.minio.ObjectWriteResponse;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;

    private final MessageUtil messageUtil;

    private final RoleRepository roleRepository;
    private final Keycloak keycloak;

    private final PasswordEncoder passwordEncoder;

    private final KeycloakUserUtil keycloakUserUtil;
    private final KeycloakRoleUtil keycloakRoleUtil;
    private final MinioAdapter minioAdapter;
    @Value("${minio.url}")
    String minioUrl;
    private final SecurityUtil securityUtil;

    private final AccountUtil accountUtil;

    public AccountServiceImpl(AccountRepository accountRepository, MessageUtil messageUtil, RoleRepository roleRepository, Keycloak keycloak, PasswordEncoder passwordEncoder, KeycloakUserUtil keycloakUserUtil, KeycloakRoleUtil keycloakRoleUtil, MinioAdapter minioAdapter, SecurityUtil securityUtil, AccountUtil accountUtil) {
        this.accountRepository = accountRepository;
        this.messageUtil = messageUtil;
        this.roleRepository = roleRepository;
        this.keycloak = keycloak;
        this.passwordEncoder = passwordEncoder;
        this.keycloakUserUtil = keycloakUserUtil;
        this.keycloakRoleUtil = keycloakRoleUtil;
        this.minioAdapter = minioAdapter;
        this.securityUtil = securityUtil;
        this.accountUtil = accountUtil;
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public User saveAccount(User user) {
        return null;
    }


    @Override
    public ApiPage<AccountResponse> getAccounts(Pageable pageable) {
        Page<User> accounts = accountRepository.findAll(pageable);
        return PageUtil.convert(accounts.map(ConvertUtil::doConvertEntityToResponse));
    }


    @Override
    public StudentResponse createStudentAccount(StudentRequest studentRequest) {


        //set Account

        if (accountRepository.existsAccountByUsername(studentRequest.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Tên đăng nhập đã tồn tại");
        }
//        if (accountDetailRepository.existsAccountDetailByPhone(studentRequest.getPhone())) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Số điện thoại đã tồn tại");
//        }
//        if (accountDetailRepository.existsAccountDetailByEmail(studentRequest.getEmail())) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Email đã tồn tại");
//        }

        if (!PasswordUtil.validationPassword(studentRequest.getPassword()) || studentRequest.getPassword() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Mật khẩu phải có ít nhất một ký tự số, ký tự viết thường, ký tự viết hoa, ký hiệu đặc biệt trong số @#$% và độ dài phải từ 8 đến 2"));
        }

        User user = new User();
        user.setUsername(studentRequest.getEmail());
        if (!EmailUtil.isValidEmail(studentRequest.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Email không đúng định dạng. ");
        }

        if (!PasswordUtil.validationPassword(studentRequest.getPassword()) || studentRequest.getPassword() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Mật khẩu phải có ít nhất một ký tự số, ký tự viết thường, ký tự viết hoa, ký hiệu đặc biệt trong số @#$% và độ dài phải từ 8 đến 20"));
        }
        user.setKeycloak(true);
        user.setIsActive(true);
        user.setPassword(PasswordUtil.BCryptPasswordEncoder(studentRequest.getPassword()));
        Role role = roleRepository.findRoleByCode(EAccountRole.STUDENT)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay role")));
        user.setRole(role);

        StudentResponse studentResponse = new StudentResponse();
        Boolean saveAccountSuccess = keycloakUserUtil.create(user);
        Boolean assignRoleSuccess = keycloakRoleUtil.assignRoleToUser(role.getCode().name(), user);
        if (saveAccountSuccess && assignRoleSuccess) {
//            account.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
            User userSave = accountRepository.save(user);
            studentResponse.setId(userSave.getId());
            studentResponse.setEmail(studentRequest.getEmail());
            studentResponse.setName(studentRequest.getFirstName() + " " + studentRequest.getLastName());
            return studentResponse;
        }
        return studentResponse;
    }


    @Override
    public Boolean uploadAvatar(long id, UploadAvatarRequest uploadAvatarRequest) {

        User teacher = securityUtil.getCurrentUserThrowNotFoundException();

        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));

        if (!teacher.getId().equals(user.getId())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Bạn không thể cập nhật avatar cho tài khoản khác!!");
        }
        List<User> users = new ArrayList<>();
        users.add(user);
        try {
            String name = uploadAvatarRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadAvatarRequest.getFile().getContentType(),
                    uploadAvatarRequest.getFile().getInputStream(), uploadAvatarRequest.getFile().getSize());

            accountRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return true;
    }

    @Override
    public AccountResponse editProfile(long id, AccountEditRequest accountEditRequest) {
        return null;
    }

    @Override
    public AccountResponse editStudentProfile(long id, AccountEditRequest accountEditRequest) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));
//        account.setBirthday(accountEditRequest.getBirthDay());
//        account.setEmail(accountEditRequest.getMail());
//        account.setFirstName(accountEditRequest.getFirstName());
//        account.setLastName(accountEditRequest.getLastName());
//        account.setPhoneNumber(accountEditRequest.getPhone());
//        account.setGender(accountEditRequest.getGender());
        User save = accountRepository.save(user);
        AccountResponse response = ObjectUtil.copyProperties(save, new AccountResponse(), AccountResponse.class);
        if (save.getRole() != null) {
            response.setRole(ObjectUtil.copyProperties(save.getRole(), new RoleDto(), RoleDto.class));
        }

        return response;
    }

    @Override
    public ApiPage<AccountResponse> getTeacherAccounts(Pageable pageable) {
        Role role = roleRepository.findRoleByCode(EAccountRole.TEACHER)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Role not found by code:" + EAccountRole.TEACHER));
        Page<User> accounts = accountRepository.findAccountByRole(pageable, role);
        return PageUtil.convert(accounts.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public ApiPage<AccountResponse> getStudentAccounts(Pageable pageable) {
        Role role = roleRepository.findRoleByCode(EAccountRole.STUDENT)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Role not found by code:" + EAccountRole.STUDENT));
        Page<User> accounts = accountRepository.findAccountByRole(pageable, role);
        return PageUtil.convert(accounts.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public Boolean banAndUbBanAccount(long id) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));
        if (user.getIsActive()) {
            user.setIsActive(false);
        } else {
            user.setIsActive(true);
        }
        accountRepository.save(user);
        return true;
    }

    @Override
    public ApiPage<AccountResponse> searchAccount(AccountSearchRequest query, Pageable pageable) {
//        AccountSpecificationBuilder builder = AccountSpecificationBuilder.specification()
//                .queryLike(query.getQ());
//
//        Page<User> accountPage = accountRepository.findAll(builder.build(), pageable);
//        return PageUtil.convert(accountPage.map(ConvertUtil::doConvertEntityToResponse));
        return null;
    }

    @Override
    public AccountResponse updateRoleAndActiveAccount(long id, AccountEditRequest accountEditRequest) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));
        Role role = roleRepository.findRoleByCode(accountEditRequest.getRole())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay role"));
        user.setRole(role);
        user.setIsActive(accountEditRequest.isActive());
        User save = accountRepository.save(user);
        return ConvertUtil.doConvertEntityToResponse(save);
    }

    @Override
    public AccountResponse approveTeacherAccount(long id) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(("Khong tim thay account") + id));
        user.setIsActive(true);
        User save = accountRepository.save(user);
        return ConvertUtil.doConvertEntityToResponse(save);
    }

    @Override
    public AccountResponse getSelfAccount() {
        User currentUser = securityUtil.getCurrentUserThrowNotFoundException();
//        accountUtil.synchronizedCurrentAccountInfo();
        return ConvertUtil.doConvertEntityToResponse(currentUser);
    }

    public AccountResponse getAccountById(long id) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));
        AccountResponse accountResponse = ConvertUtil.doConvertEntityToResponse(user);
        return accountResponse;
    }


    @Override
    public AccountResponse createManagerOrAccountant(CreateAccountRequest request) {

        EAccountRole roleCode = request.getRole();
        if (roleCode == null || !(roleCode.equals(EAccountRole.ACCOUNTANT) || roleCode.equals(EAccountRole.MANAGER))) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Chỉ có thể tạo tài khoản cho Quản lý và Kế toán!"));
        }
        User user = new User();
        if (accountRepository.existsAccountByUsername(request.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Username đã tồn tại"));
        }

        user.setUsername(request.getEmail());
        Role role = roleRepository.findRoleByCode(roleCode)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Không tìm thấy role!")));
        user.setRole(role);

        User save = accountRepository.save(user);
        user.setPassword(PasswordUtil.BCryptPasswordEncoder(request.getPassword()));
        Boolean saveAccountSuccess = keycloakUserUtil.create(user);
        Boolean assignRoleSuccess = keycloakRoleUtil.assignRoleToUser(role.getCode().name(), user);
        if (saveAccountSuccess && assignRoleSuccess) {
            return ConvertUtil.doConvertEntityToResponse(save);
        } else {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Tạo tài khoản thất bại, vui lòng thử lại!");
        }
    }

}
