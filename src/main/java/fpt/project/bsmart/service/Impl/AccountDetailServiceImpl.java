package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.*;
import fpt.project.bsmart.entity.dto.EmailDto;
import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.AccountDetailEditRequest;
import fpt.project.bsmart.entity.request.AccountDetailRequest;
import fpt.project.bsmart.entity.request.RequestEditAccountDetailRequest;
import fpt.project.bsmart.entity.request.UploadAvatarRequest;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import fpt.project.bsmart.entity.response.FeedbackAccountLogResponse;
import fpt.project.bsmart.entity.response.ResponseAccountDetailResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IAccountDetailService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import fpt.project.bsmart.util.keycloak.KeycloakRoleUtil;
import fpt.project.bsmart.util.keycloak.KeycloakUserUtil;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class AccountDetailServiceImpl implements IAccountDetailService {
    private final AccountRepository accountRepository;

    private final MessageUtil messageUtil;

    private final PasswordEncoder passwordEncoder;

    private final AccountUtil accountUtil;

    private final VoiceRepository voiceRepository;

    private final AccountDetailRepository accountDetailRepository;

    private final FeedbackAccountLogRepository feedbackAccountLogRepository;


    private final KeycloakUserUtil keycloakUserUtil;
    private final KeycloakRoleUtil keycloakRoleUtil;

    private final MinioAdapter minioAdapter;

    private final ResourceRepository resourceRepository;

    private final RoleRepository roleRepository;

    private final SecurityUtil securityUtil;

    private final SubjectRepository subjectRepository;

    private final ClassLevelRepository classLevelRepository;

    private final SendMailServiceImplServiceImpl sendMailServiceImplService;

    private final AccountDetailClassLevelRepository accountDetailClassLevelRepository;

    private final AccountDetailSubjectRepository accountDetailSubjectRepository;


    @Value("${minio.url}")
    String minioUrl;

    public AccountDetailServiceImpl(AccountRepository accountRepository, MessageUtil messageUtil, PasswordEncoder passwordEncoder, AccountUtil accountUtil, VoiceRepository voiceRepository, AccountDetailRepository accountDetailRepository, FeedbackAccountLogRepository feedbackAccountLogRepository, KeycloakUserUtil keycloakUserUtil, KeycloakRoleUtil keycloakRoleUtil, MinioAdapter minioAdapter, ResourceRepository resourceRepository, RoleRepository roleRepository, SecurityUtil securityUtil, SubjectRepository subjectRepository, ClassLevelRepository classLevelRepository, SendMailServiceImplServiceImpl sendMailServiceImplService, AccountDetailClassLevelRepository accountDetailClassLevelRepository, AccountDetailSubjectRepository accountDetailSubjectRepository) {
        this.accountRepository = accountRepository;
        this.messageUtil = messageUtil;
        this.passwordEncoder = passwordEncoder;
        this.accountUtil = accountUtil;
        this.voiceRepository = voiceRepository;
        this.accountDetailRepository = accountDetailRepository;
        this.feedbackAccountLogRepository = feedbackAccountLogRepository;
        this.keycloakUserUtil = keycloakUserUtil;
        this.keycloakRoleUtil = keycloakRoleUtil;
        this.minioAdapter = minioAdapter;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
        this.securityUtil = securityUtil;
        this.subjectRepository = subjectRepository;
        this.classLevelRepository = classLevelRepository;
        this.sendMailServiceImplService = sendMailServiceImplService;
        this.accountDetailClassLevelRepository = accountDetailClassLevelRepository;
        this.accountDetailSubjectRepository = accountDetailSubjectRepository;
    }


    public Boolean checkBirthday(String birthday) {

        LocalDate today = LocalDate.now();

        Instant instant = Instant.parse(birthday);
        LocalDate localDate
                = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();

        Period p = Period.between(localDate, today);
        int checkBirthday = p.getYears();
        System.out.println("You are " + p.getYears() + " years");
        if (checkBirthday <= 18) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Bạn chưa đủ 18 tuổi"));
        }
        return true;
    }


    @Override
    public Long registerTutor(AccountDetailRequest accountDetailRequest) {


        AccountDetail accountDetail = new AccountDetail();
        if (accountDetailRequest.getUsername() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Tài khoản không được để trống"));
        }
        if (accountRepository.existsAccountByUsername(accountDetailRequest.getUsername())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Tài khoản đã tồn tại"));
        }
        if (accountDetailRequest.getEmail() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Email không được để trống"));
        }
        if (accountDetailRepository.existsAccountByEmail(accountDetailRequest.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Email đã đã tồn tại"));
        }
        if (accountDetailRepository.existsAccountDetailByPhone(accountDetailRequest.getPhone()) || accountDetailRequest.getPhone() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Số điên thoại đã được đăng ký trong hệ thống"));
        }


        accountDetail.setLastName(accountDetailRequest.getLastName());
        accountDetail.setFirstName(accountDetailRequest.getFirstName());

        accountDetail.setBirthDay(accountDetailRequest.getBirthDay());

        //Nguyên quán

        accountDetail.setGender(accountDetailRequest.getGender());
        accountDetail.setCurrentAddress(accountDetailRequest.getCurrentAddress());
        if (accountDetailRepository.existsAccountDetailByIdCard(accountDetailRequest.getIdCard()) || accountDetailRequest.getIdCard() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Số chứng minh / căn cước công dân đã có trong hệ thống"));
        }

        accountDetail.setIdCard(accountDetailRequest.getIdCard());
        accountDetail.setEmail(accountDetailRequest.getEmail());
        accountDetail.setPhone(accountDetailRequest.getPhone());
        if (!PasswordUtil.validationPassword(accountDetailRequest.getPassword()) || accountDetailRequest.getPassword() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Mật khẩu phải có ít nhất một ký tự số, ký tự viết thường, ký tự viết hoa, ký hiệu đặc biệt trong số @#$% và độ dài phải từ 8 đến 20"));
        }
        accountDetail.setPassword(accountDetailRequest.getPassword());
        // tên trươnng đh / cao đang đã học
        accountDetail.setTrainingSchoolName(accountDetailRequest.getTrainingSchoolName());
        // ngành học
        accountDetail.setMajors(accountDetailRequest.getMajors());
        // trinh độ : h , cao đẳng
        accountDetail.setLevel(accountDetailRequest.getLevel());
        List<Long> subjects = accountDetailRequest.getSubjects();
        List<AccountDetailSubject> accountDetailSubjectList = new ArrayList<>();
        if (subjects != null) {
            List<Subject> allSubjects = subjectRepository.findAllById(subjects);


            allSubjects.forEach(subject -> {
                AccountDetailSubject accountDetailSubject = new AccountDetailSubject();
                AccountDetailSubjectKey accountDetailSubjectKey = new AccountDetailSubjectKey();
                accountDetailSubjectKey.setSubjectId(subject.getId());
                accountDetailSubjectKey.setAccountDetailId(accountDetail.getId());
                accountDetailSubject.setId(accountDetailSubjectKey);
                accountDetailSubject.setSubject(subject);
                accountDetailSubject.setAccountDetail(accountDetail);
                accountDetailSubjectList.add(accountDetailSubject);
            });
        }


        accountDetail.setAccountDetailSubjects(accountDetailSubjectList);


        List<AccountDetailClassLevel> accountDetailClassLevelList = new ArrayList<>();
        List<Long> classLevels = accountDetailRequest.getClassLevels();
        if (classLevels != null) {
            List<ClassLevel> allClassLevel = classLevelRepository.findAllById(classLevels);
            if (allClassLevel.isEmpty()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Bạn chưa chọn lớp dạy !"));
            }

            allClassLevel.forEach(classLevel -> {
                AccountDetailClassLevel accountDetailClassLevel = new AccountDetailClassLevel();
                AccountDetailClassLevelKey accountDetailClassLevelKey = new AccountDetailClassLevelKey();
                accountDetailClassLevelKey.setClassLevelId(classLevel.getId());
                accountDetailClassLevelKey.setAccountDetailId(accountDetail.getId());
                accountDetailClassLevel.setId(accountDetailClassLevelKey);
                accountDetailClassLevel.setAccountDetail(accountDetail);
                accountDetailClassLevel.setClassLevel(classLevel);
                accountDetailClassLevelList.add(accountDetailClassLevel);
            });
        }


        accountDetail.setAccountDetailClassLevels(accountDetailClassLevelList);

        Voice voice = voiceRepository.findById(accountDetailRequest.getVoiceId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("không tìm thấy giọng")));

        accountDetail.setVoice(voice);
        accountDetail.setStatus(EAccountDetailStatus.REQUESTING);
        accountDetail.setActive(true);


        Account account = new Account();
        if (accountRepository.existsAccountByUsername(accountDetailRequest.getUsername())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Tên đăng nhập đã tồn tại"));
        }
        account.setUsername(accountDetailRequest.getUsername());
        account.setIsActive(true);
        account.setKeycloak(true);
        account.setPassword(accountDetail.getPassword());
        Role role = roleRepository.findRoleByCode(EAccountRole.TEACHER)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay role")));
        account.setRole(role);


        accountDetail.setAccount(account);

        Boolean saveAccountSuccess = keycloakUserUtil.create(account);
        Boolean assignRoleSuccess = keycloakRoleUtil.assignRoleToUser(role.getCode().name(), account);

//        List<EmailDto> mailList = new ArrayList<>();
//        EmailDto emailDto = new EmailDto();
//
//        emailDto.setName(accountDetail.getFirstName() + "" + accountDetail.getLastName());
//        emailDto.setPassword(accountDetail.getPassword());
//
//        mailList.add(emailDto);
//        String passMail = "qpdrqauknkrxtsrs" ;
//        Boolean aBoolean = sendMailServiceImplService.sendMailToRegisterDoTeacher(mailList, accountDetail, passMail);
//        AccountDetail save = null;
//        if (aBoolean) {
//            save = accountDetailRepository.save(accountDetail);
//        }

        AccountDetail save = accountDetailRepository.save(accountDetail);
        return save.getId();
    }


    @Override
    public List<ResourceDto> uploadImageRegisterProfile(Long id, UploadAvatarRequest uploadImageRequest) {

        AccountDetail accountDetail = accountDetailRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay account" + id));

        List<Resource> resourceList = new ArrayList<>();

        try {
            String name = uploadImageRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadImageRequest.getFile().getContentType(),
                    uploadImageRequest.getFile().getInputStream(), uploadImageRequest.getFile().getSize());

            Resource resource = new Resource();
            resource.setName(name);
            resource.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
            resource.setAccountDetail(accountDetail);
            if (uploadImageRequest.getResourceType().equals(EResourceType.CARTPHOTO)) {
                resource.setResourceType(EResourceType.CARTPHOTO);
            } else if (uploadImageRequest.getResourceType().equals(EResourceType.DEGREE)) {
                resource.setResourceType(EResourceType.DEGREE);
            } else if (uploadImageRequest.getResourceType().equals(EResourceType.CCCDONE)) {
                resource.setResourceType(EResourceType.CCCDONE);

            } else if (uploadImageRequest.getResourceType().equals(EResourceType.CCCDTWO)) {
                resource.setResourceType(EResourceType.CCCDTWO);
            }

            resourceList.add(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        resourceRepository.saveAll(resourceList);

        List<ResourceDto> resourceDtoList = new ArrayList<>();
        resourceList.forEach(resource -> {
            ResourceDto resourceDto = ObjectUtil.copyProperties(resource, new ResourceDto(), ResourceDto.class);
            resourceDto.setResourceType(resource.getResourceType());
            resourceDtoList.add(resourceDto);
        });
        return resourceDtoList;
    }

    @Override
    public ResponseAccountDetailResponse approveRegisterAccount(RequestEditAccountDetailRequest editAccountDetailRequest) {
        ResponseAccountDetailResponse response = new ResponseAccountDetailResponse();
        List<AccountDetail> accountDetailList = new ArrayList<>();
        Account teacher = accountRepository.findById(editAccountDetailRequest.getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay tài khoản")));
        List<FeedbackAccountLog> feedbackAccountLogs = new ArrayList<>();

        AccountDetail accountDetail = teacher.getAccountDetail();
        if (accountDetail != null) {
            EmailDto emailDto = new EmailDto();

            if (accountDetail.getEmail() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Không thể phê duyệt tài khoản vì không có email"));
            }
            if (accountDetail.getPassword() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Không thể phê duyệt tài khoản vì không có password"));
            }

            FeedbackAccountLog log = new FeedbackAccountLog();
            log.setAccountDetail(accountDetail);
            log.setAccount(teacher);
            log.setStatus(EFeedbackAccountLogStatus.APPROVE);
            log.setContent(editAccountDetailRequest.getContent());
            feedbackAccountLogs.add(log);
            accountDetail.setFeedbackAccountLogs(feedbackAccountLogs);

            accountDetail.setStatus(EAccountDetailStatus.APPROVE);
            accountDetail.setPassword(PasswordUtil.BCryptPasswordEncoder(accountDetail.getPassword()));

            accountDetailList.add(accountDetail);


            AccountDetailResponse accountDetailResponse = ConvertUtil.doConvertEntityToResponse(accountDetail);
            List<FeedbackAccountLogResponse> feedbackAccountLogResponseList = new ArrayList<>();
            response.setAccountDetail(accountDetailResponse);
            feedbackAccountLogs.forEach(feedbackAccountLog -> {
                feedbackAccountLogResponseList.add(ConvertUtil.doConvertEntityToResponse(log));
            });
            response.setFeedbackAccountLog(feedbackAccountLogResponseList);
        }


        List<FeedbackAccountLog> feedbackAccountLog = feedbackAccountLogRepository.saveAll(feedbackAccountLogs);
        accountUtil.synchronizedCurrentAccountInfo();

        return response;
    }

    @Override
    public ResponseAccountDetailResponse requestEditRegisterAccount(RequestEditAccountDetailRequest editAccountDetailRequest) {
        ResponseAccountDetailResponse response = new ResponseAccountDetailResponse();
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
//        List<EmailDto> mail = new ArrayList<>();
        List<AccountDetail> accountDetailList = new ArrayList<>();

        Account account = accountRepository.findById(editAccountDetailRequest.getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("tài khoản không tìm thấy"));

        List<FeedbackAccountLog> feedbackAccountLogs = new ArrayList<>();

        AccountDetail accountDetail = account.getAccountDetail();
        if (accountDetail != null) {
            if (accountDetail.getEmail() == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage("Không thể phê duyệt tài khoản vì không có email"));
            }
            if (accountDetail.getPassword() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Không thể phê duyệt tài khoản vì không có password"));
            }

            accountDetail.setStatus(EAccountDetailStatus.EDITREQUEST);

            FeedbackAccountLog log = new FeedbackAccountLog();
            log.setAccountDetail(accountDetail);
            log.setAccount(teacher);
            log.setStatus(EFeedbackAccountLogStatus.EDITREQUEST);
            log.setContent(editAccountDetailRequest.getContent());
            feedbackAccountLogs.add(log);

            accountDetail.setFeedbackAccountLogs(feedbackAccountLogs);


            accountDetailList.add(accountDetail);

            EmailDto emailDto = new EmailDto();
            emailDto.setMail(accountDetail.getEmail());
            emailDto.setMail(accountDetail.getEmail());


            sendMailServiceImplService.sendMail(emailDto, Constants.MailMessage.SUBJECT_MAIL_EDIT_REQUEST_PROFILE
                    , editAccountDetailRequest.getContent(), Constants.MailMessage.FOOTER_MAIL_EDIT_REQUEST_PROFILE);


            AccountDetailResponse accountDetailResponse = ConvertUtil.doConvertEntityToResponse(accountDetail);
            response.setAccountDetail(accountDetailResponse);
            response.setAccountDetail(accountDetailResponse);


        }

        List<FeedbackAccountLogResponse> feedbackAccountLogResponseList = new ArrayList<>();
        List<FeedbackAccountLog> feedbackAccountLog = feedbackAccountLogRepository.saveAll(feedbackAccountLogs);

        feedbackAccountLogs.forEach(log -> {
            feedbackAccountLogResponseList.add(ConvertUtil.doConvertEntityToResponse(log));
        });
        response.setFeedbackAccountLog(feedbackAccountLogResponseList);


        return response;
    }

    @Override
    public ResponseAccountDetailResponse refuseRegisterAccount(RequestEditAccountDetailRequest editAccountDetailRequest) {
        ResponseAccountDetailResponse response = new ResponseAccountDetailResponse();
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
//        List<EmailDto> mail = new ArrayList<>();
        List<AccountDetail> accountDetailList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        ids.add(editAccountDetailRequest.getId());
        Account account = accountRepository.findById(editAccountDetailRequest.getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay tài khoản")));

        List<FeedbackAccountLog> feedbackAccountLogs = new ArrayList<>();

        AccountDetail accountDetail = account.getAccountDetail();
        if (accountDetail != null) {
//            EmailDto emailDto = new EmailDto();

            accountDetail.setStatus(EAccountDetailStatus.REFUSE);

            FeedbackAccountLog log = new FeedbackAccountLog();
            log.setAccountDetail(accountDetail);
            log.setAccount(teacher);
            log.setStatus(EFeedbackAccountLogStatus.REFUSE);
            log.setContent(editAccountDetailRequest.getContent());
            feedbackAccountLogs.add(log);

            accountDetail.setFeedbackAccountLogs(feedbackAccountLogs);


            accountDetailList.add(accountDetail);
//            emailDto.setMail(accountDetail.getEmail());
//            emailDto.setName(accountDetail.getFirstName() + "" + accountDetail.getLastName());
//            emailDto.setPassword(accountDetail.getPassword());

//            mail.add(emailDto);
            AccountDetailResponse accountDetailResponse = ConvertUtil.doConvertEntityToResponse(accountDetail);

            response.setAccountDetail(accountDetailResponse);
            List<FeedbackAccountLogResponse> feedbackAccountLogResponseList = new ArrayList<>();
            response.setAccountDetail(accountDetailResponse);
            feedbackAccountLogs.forEach(feedbackAccountLog -> {
                feedbackAccountLogResponseList.add(ConvertUtil.doConvertEntityToResponse(log));
            });
            response.setFeedbackAccountLog(feedbackAccountLogResponseList);
        }


        List<FeedbackAccountLog> feedbackAccountLog = feedbackAccountLogRepository.saveAll(feedbackAccountLogs);


//        sendMailServiceImplService.sendMail(mail);
        return response;
    }

    @Override
    public Long teacherUpdateProfileForAdmin(AccountDetailEditRequest editAccountDetailRequest) {
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
//        AccountDetail accountDetail = accountDetailRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay tài khoản")));
        AccountDetail accountDetail = teacher.getAccountDetail();
//        if (!accountDetail.getStatus().equals(EAccountDetailStatus.EDITREQUEST)) {
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Tài Khoản không được cập nhật vì trạng thái không cho phép ");
//        }

        if (accountDetail == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Tài Khoản không tồn tại");
        }


        accountDetail.setLastName(editAccountDetailRequest.getLastName());
        accountDetail.setFirstName(editAccountDetailRequest.getFirstName());

        accountDetail.setBirthDay(editAccountDetailRequest.getBirthDay());


        accountDetail.setGender(editAccountDetailRequest.getGender());
        accountDetail.setCurrentAddress(editAccountDetailRequest.getCurrentAddress());

        if (!accountDetailRepository.existsAccountDetailByIdCard(editAccountDetailRequest.getIdCard()) &&
                !editAccountDetailRequest.getIdCard().equals(accountDetail.getIdCard())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Số chứng minh / căn cước công dân đã có trong hệ thống");
        }
        accountDetail.setLevel(editAccountDetailRequest.getLevel());
        accountDetail.setIdCard(editAccountDetailRequest.getIdCard());
        if (!accountDetailRepository.existsAccountDetailByPhone(editAccountDetailRequest.getIdCard()) &&
                !editAccountDetailRequest.getPhone().equals(accountDetail.getPhone())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Số điện thoại này  đã dùng đăng ký trong hệ thống");
        }

        accountDetail.setPhone(editAccountDetailRequest.getPhone());
//        if (!PasswordUtil.validationPassword(editAccountDetailRequest.getPassword()) || editAccountDetailRequest.getPassword() == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage("Mật khẩu phải có ít nhất một ký tự số, ký tự viết thường, ký tự viết hoa, ký hiệu đặc biệt trong số @#$% và độ dài phải từ 8 đến 20"));
//        }
//        accountDetail.setPassword(editAccountDetailRequest.getPassword());
        // tên trươnng đh / cao đang đã học
        accountDetail.setTrainingSchoolName(editAccountDetailRequest.getTrainingSchoolName());
        // ngành học
        accountDetail.setMajors(editAccountDetailRequest.getMajors());
        // trinh độ : h , cao đẳng

        List<AccountDetailClassLevel> accountDetailClassLevelList = new ArrayList<>();
        List<Long> classLevels = editAccountDetailRequest.getClassLevels();
        if (classLevels == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Bạn chưa chọn lớp dạy !"));

        }

        List<AccountDetailClassLevel> allByAccountDetail = accountDetailClassLevelRepository.findAllByAccountDetail(accountDetail);
        accountDetailClassLevelRepository.deleteAll(allByAccountDetail);

        List<AccountDetailSubject> allByAccountDetail1 = accountDetailSubjectRepository.findAllByAccountDetail(accountDetail);
        accountDetailSubjectRepository.deleteAll(allByAccountDetail1);

//        List<Resource> allByAccountDetail2 = resourceRepository.findAllByAccountDetail(accountDetail);
//        resourceRepository.deleteAll(allByAccountDetail2);
        for (Long classLevelId : classLevels) {

            AccountDetailClassLevel accountDetailClassLevel = new AccountDetailClassLevel();
            AccountDetailClassLevelKey accountDetailClassLevelKey = new AccountDetailClassLevelKey();
            accountDetailClassLevelKey.setClassLevelId(classLevelId);
            accountDetailClassLevelKey.setAccountDetailId(accountDetail.getId());
            accountDetailClassLevel.setId(accountDetailClassLevelKey);
            accountDetailClassLevel.setAccountDetail(accountDetail);
            ClassLevel classLevel = classLevelRepository.findById(classLevelId)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Không tim thấy lớp dạy")));
            accountDetailClassLevel.setClassLevel(classLevel);
            AccountDetailClassLevel byAccountDetailAndClassLevel = accountDetailClassLevelRepository.findByAccountDetailAndClassLevel(accountDetail, classLevel);

            accountDetailClassLevelList.add(accountDetailClassLevel);

        }

        accountDetail.getAccountDetailClassLevels().addAll(accountDetailClassLevelList);


        List<Long> subjects = editAccountDetailRequest.getSubjects();
        List<AccountDetailSubject> accountDetailSubjectList = new ArrayList<>();

        for (Long subjectId : subjects) {
            AccountDetailSubject accountDetailSubject = new AccountDetailSubject();
            AccountDetailSubjectKey accountDetailSubjectKey = new AccountDetailSubjectKey();
            accountDetailSubjectKey.setSubjectId(subjectId);
            accountDetailSubjectKey.setAccountDetailId(accountDetail.getId());
            accountDetailSubject.setId(accountDetailSubjectKey);
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Không tim thấy lớp môn")));
            accountDetailSubject.setSubject(subject);
            accountDetailSubject.setAccountDetail(accountDetail);
            accountDetailSubjectList.add(accountDetailSubject);
        }

        accountDetail.getAccountDetailSubjects().addAll(accountDetailSubjectList);


        Voice voice = voiceRepository.findById(editAccountDetailRequest.getVoiceId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("không tìm thấy giọng")));
        accountDetail.setVoice(voice);

        accountDetail.setStatus(EAccountDetailStatus.REQUESTING);
        accountDetail.setActive(true);


//        List<Resource> resourceList = new ArrayList<>();

//        for (UploadAvatarRequest uploadImageRequest : editAccountDetailRequest.getFiles()) {
//            try {
//                String name = uploadImageRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
//                ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadImageRequest.getFile().getContentType(),
//                        uploadImageRequest.getFile().getInputStream(), uploadImageRequest.getFile().getSize());
//
//                Resource resource = new Resource();
//                resource.setName(name);
//                resource.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
//                resource.setAccountDetail(accountDetail);
//                if (uploadImageRequest.getResourceType().equals(EResourceType.CARTPHOTO)) {
//                    resource.setResourceType(EResourceType.CARTPHOTO);
//                } else if (uploadImageRequest.getResourceType().equals(EResourceType.DEGREE)) {
//                    resource.setResourceType(EResourceType.CARTPHOTO);
//                } else if (uploadImageRequest.getResourceType().equals(EResourceType.CCCDONE)) {
//                    resource.setResourceType(EResourceType.CCCDONE);
//
//                } else if (uploadImageRequest.getResourceType().equals(EResourceType.CCCDTWO)) {
//                    resource.setResourceType(EResourceType.CCCDTWO);
//                }
//                resourceList.add(resource);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        if (resourceList.size() < 4) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage("Hệ thống cần bạn upload đầy đủ hình ảnh."));
//        }
//
//        accountDetail.getResources().addAll(resourceList);

        accountDetailRepository.save(accountDetail);


        return accountDetail.getId();
    }


    @Override
    public ApiPage<AccountDetailResponse> getRequestToActiveAccount(EAccountDetailStatus status, Pageable pageable) {

        Role role = roleRepository.findRoleByCode(EAccountRole.TEACHER)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay role")));

        Page<Account> accounts = null;

//        status = EAccountDetailStatus.REQUESTING;


        accounts = accountRepository.findAccountByRoleAndAccountDetailStatus(role, status, pageable);

        return PageUtil.convert(accounts.map(account -> ConvertUtil.doConvertEntityToResponse(account.getAccountDetail())));

    }

    @Override
    public ResponseAccountDetailResponse teacherGetInfo() {
        ResponseAccountDetailResponse response = new ResponseAccountDetailResponse();
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        AccountDetailResponse accountDetailResponse = ConvertUtil.doConvertEntityToResponse(teacher.getAccountDetail());
        response.setAccountDetail(accountDetailResponse);

        List<FeedbackAccountLogResponse> feedbackAccountLogResponseList = new ArrayList<>();
        if (teacher.getAccountDetail() != null) {
            List<FeedbackAccountLog> feedbackAccountLogList = feedbackAccountLogRepository.findFeedbackAccountLogByAccountDetailId(teacher.getAccountDetail().getId());

            feedbackAccountLogList.forEach(feedbackAccountLog -> {
                FeedbackAccountLogResponse convertEntityToResponse = ConvertUtil.doConvertEntityToResponse(feedbackAccountLog);
                feedbackAccountLogResponseList.add(convertEntityToResponse);
            });
        }

        response.setFeedbackAccountLog(feedbackAccountLogResponseList);
        return response;

    }


    @Override
    public AccountDetailResponse getAccountDetail(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay account")));
        return ConvertUtil.doConvertEntityToResponse(account.getAccountDetail());
    }


}