package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.config.security.oauth2.dto.LocalUser;
import fpt.project.bsmart.config.security.oauth2.dto.SignUpRequest;
import fpt.project.bsmart.config.security.oauth2.user.OAuth2UserInfo;
import fpt.project.bsmart.config.security.oauth2.user.OAuth2UserInfoFactory;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.dto.mentor.TeachInformationDTO;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.*;
import fpt.project.bsmart.entity.response.VerifyResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IUserService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import fpt.project.bsmart.util.email.EmailUtil;
import fpt.project.bsmart.util.specification.ClassSpecificationBuilder;
import fpt.project.bsmart.util.specification.FeedbackSubmissionSpecificationBuilder;
import fpt.project.bsmart.util.specification.UserSpecificationBuilder;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Service
@Transactional
public class UserServiceImpl implements IUserService {


    @Value("${minio.endpoint}")
    String minioUrl;
    private final UserRepository userRepository;

    private final MessageUtil messageUtil;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final ImageRepository imageRepository;

    private final UserImageRepository userImageRepository;
    private final MentorProfileRepository mentorProfileRepository;

    private final MinioAdapter minioAdapter;
    private final EmailUtil emailUtil;

    private final VerificationRepository verificationRepository;

    private final NotificationRepository notificationRepository;
    private final ClassRepository classRepository;
    private final FeedbackSubmissionRepository feedbackSubmissionRepository;
    private final WebSocketUtil webSocketUtil;


    public UserServiceImpl(UserRepository userRepository, MessageUtil messageUtil, RoleRepository roleRepository, PasswordEncoder encoder, ImageRepository imageRepository, UserImageRepository userImageRepository, MentorProfileRepository mentorProfileRepository, MinioAdapter minioAdapter, EmailUtil emailUtil, VerificationRepository verificationRepository, NotificationRepository notificationRepository, ClassRepository classRepository, FeedbackSubmissionRepository feedbackSubmissionRepository, WebSocketUtil webSocketUtil) {
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.imageRepository = imageRepository;
        this.userImageRepository = userImageRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.minioAdapter = minioAdapter;
        this.emailUtil = emailUtil;
        this.verificationRepository = verificationRepository;
        this.notificationRepository = notificationRepository;
        this.classRepository = classRepository;
        this.feedbackSubmissionRepository = feedbackSubmissionRepository;
        this.webSocketUtil = webSocketUtil;
    }

    private static void accept(UserImage userImage) {
        userImage.setStatus(false);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID) + id));
    }


    @Override
    public ApiPage<UserDto> adminGetAllUser(UserSearchRequest request, Pageable pageable) {

        UserSpecificationBuilder builder = UserSpecificationBuilder.specificationBuilder().queryLike(request.getQ()).hasRole(request.getRole()).isVerified(request.getIsVerified());
        Page<User> userPage = userRepository.findAll(builder.build(), pageable);
        List<UserDto> userInfoResponses = userPage.getContent().stream().map(ConvertUtil::convertUsertoUserDto).collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(userInfoResponses, pageable, userPage.getTotalElements()));
    }

    public User getCurrentLoginUser() {
        return SecurityUtil.getCurrentUser();
    }

    @Override
    public UserDto getLoginUser() {
        User currentLoginUser = getCurrentLoginUser();
        return ConvertUtil.convertUsertoUserDto(currentLoginUser);
    }

    public UserDto getUserProfileForMentorPage(Long id) {
        User user = findUserById(id);
        Boolean isMentor = SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER) && user.getMentorProfile().getStatus().equals(EMentorProfileStatus.STARTING);
        if (!isMentor) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID) + id);
        }
        UserDto userDto = ConvertUtil.convertUserForMentorProfilePage(user);
        TeachInformationDTO teachInformationDTO = new TeachInformationDTO();
        ClassSpecificationBuilder classSpecificationBuilder = ClassSpecificationBuilder.classSpecificationBuilder().byMentor(user).filterByStatus(ECourseStatus.ENDED);
        List<Class> classes = classRepository.findAll(classSpecificationBuilder.build());
        Integer numberOfMember = classes.stream().map(Class::getStudentClasses).distinct().collect(Collectors.toList()).stream().map(x -> x.size()).mapToInt(Integer::intValue).sum();
        FeedbackSubmissionSpecificationBuilder feedbackSubmissionSpecificationBuilder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder().filterByMentor(id);
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(feedbackSubmissionSpecificationBuilder.build());
        teachInformationDTO.setNumberOfCourse(user.getCourses().size());
        teachInformationDTO.setNumberOfClass(classes.size());
        teachInformationDTO.setNumberOfMember(numberOfMember);
        teachInformationDTO.setNumberOfFeedBack(feedbackSubmissions.size());
        teachInformationDTO.setScoreFeedback(FeedbackUtil.calculateCourseRate(feedbackSubmissions));
        userDto.setTeachInformation(teachInformationDTO);
        return userDto;
    }

    @Override
    public Long removeSocialLink(String link) {
        if (StringUtil.isNullOrEmpty(link)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(INVALID_SOCIAL_LINK);
        }
        User user = getCurrentLoginUser();
        if (user.getFacebookLink().equals(link)) {
            user.setFacebookLink(null);
        } else if (user.getLinkedinLink().equals(link)) {
            user.setLinkedinLink(null);
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(INVALID_SOCIAL_LINK);
        }
        return userRepository.save(user).getId();
    }

    public Long uploadImageProfile(UploadImageRequest uploadImageRequest) throws IOException {
        User user = getCurrentLoginUser();
        if (user.getMentorProfile() != null) {
            MentorUtil.checkMentorStatusToUpdateInformation(user.getMentorProfile());
        }
        List<UserImage> userImages = user.getUserImages();

        if (uploadImageRequest.getImageType().equals(EImageType.AVATAR)) {
            List<UserImage> avatarCurrent = userImages.stream().filter(image -> image.getType().equals(EImageType.AVATAR)).collect(Collectors.toList());
            avatarCurrent.forEach(image1 -> {
                accept(image1);
                userImages.add(image1);
            });
        }

        if (uploadImageRequest.getImageType().equals(EImageType.FRONTCI)) {
            List<UserImage> CIFrontCurrent = userImages.stream().filter(image -> image.getType().equals(EImageType.FRONTCI)).collect(Collectors.toList());
            CIFrontCurrent.forEach(image -> {
                accept(image);
                userImages.add(image);
            });

        }
        if (uploadImageRequest.getImageType().equals(EImageType.BACKCI)) {
            List<UserImage> CIBackCurrent = userImages.stream().filter(image -> image.getType().equals(EImageType.BACKCI)).collect(Collectors.toList());
            CIBackCurrent.forEach(image -> {
                accept(image);
                userImages.add(image);
            });

        }

        userImageRepository.saveAll(userImages);
        UserImage image = new UserImage();
        String name = uploadImageRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
        ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadImageRequest.getFile().getContentType(), uploadImageRequest.getFile().getInputStream(), uploadImageRequest.getFile().getSize());
        image.setName(name);
        image.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
        image.setUser(user);
        image.setStatus(true);
        image.setVerified(true);
        if (uploadImageRequest.getImageType().equals(EImageType.AVATAR)) {
            image.setType(EImageType.AVATAR);
        }
        if (uploadImageRequest.getImageType().equals(EImageType.FRONTCI)) {
            image.setType(EImageType.FRONTCI);
        }
        if (uploadImageRequest.getImageType().equals(EImageType.BACKCI)) {
            image.setType(EImageType.BACKCI);
        }
        return userImageRepository.save(image).getId();

    }

    @Override
    public Boolean uploadDegree(List<Long> degreeIdsToDelete, MultipartFile[] file) throws IOException {
        User user = getCurrentLoginUser();
        List<UserImage> userImages = user.getUserImages();

        List<UserImage> allOldDegree = userImages.stream().filter(image -> image.getType().equals(EImageType.DEGREE)).collect(Collectors.toList());

        // id degree trong db ne
        List<Long> allOldDegreeId = allOldDegree.stream().map(UserImage::getId).collect(Collectors.toList());

        List<Long> idsToDelete = degreeIdsToDelete != null ? degreeIdsToDelete : Collections.emptyList();


        List<UserImage> degreeToDelete = new ArrayList<>();

        for (Long aLong : idsToDelete) {
            if (allOldDegreeId.contains(aLong)) {
                Optional<UserImage> byId = userImageRepository.findById(aLong);
                byId.ifPresent(degreeToDelete::add);
            }


        }


        user.getUserImages().removeAll(degreeToDelete);
        userRepository.save(user);

        MultipartFile[] files = file != null ? file : new MultipartFile[0];

        for (MultipartFile file1 : files) {
            if (!Objects.equals(file1.getOriginalFilename(), "")) {
                UserImage image = new UserImage();
                String name = file1.getOriginalFilename() + "-" + Instant.now().toString();
                ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, file1.getContentType(), file1.getInputStream(), file1.getSize());
                image.setName(name);
                image.setStatus(true);
                image.setVerified(true);
                image.setType(EImageType.DEGREE);
                image.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
                image.setUser(user);
                userImageRepository.save(image);
            }
        }


        return true;
    }


    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto = ConvertUtil.convertUsertoUserDto(findUserById(id));
        userDto.setWallet(null);
        return userDto;
    }

    @Override
    public Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest) {
        User user = getCurrentLoginUser();

        if (socialProfileEditRequest.getFacebookLink() != null) {
            if (socialProfileEditRequest.getFacebookLink().isEmpty()) {
                user.setFacebookLink(null);
            } else if (!StringUtil.isValidFacebookLink(socialProfileEditRequest.getFacebookLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FACEBOOK_LINK));
            }
            user.setFacebookLink(socialProfileEditRequest.getFacebookLink());
        }

        if (socialProfileEditRequest.getLinkedinLink() != null) {
            if (socialProfileEditRequest.getLinkedinLink().isEmpty()) {
                user.setLinkedinLink(null);
            } else if (!StringUtil.isValidLinkedinLink(socialProfileEditRequest.getLinkedinLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_TWITTER_LINK));
            }
            user.setLinkedinLink(socialProfileEditRequest.getLinkedinLink());
        }

        boolean isTeacher = SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER);
        if (isTeacher) {
            if (socialProfileEditRequest.getWebsite() != null) {
                if (socialProfileEditRequest.getWebsite().isEmpty()) {
                    user.setWebsite(null);
                } else if (!StringUtil.isValidWebsite(socialProfileEditRequest.getWebsite())) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_WEBSITE));
                }
                user.setWebsite(socialProfileEditRequest.getWebsite());
            }
        }
        return userRepository.save(user).getId();
    }

    @Override
    public Long changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentLoginUser();
        if (user != null) {
            if (changePasswordRequest.getOldPassword().isEmpty() || changePasswordRequest.getNewPassword().isEmpty()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_PASSWORD));
            }

            if (!PasswordUtil.validationPassword(changePasswordRequest.getNewPassword())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PASSWORD));
            }
            if (!PasswordUtil.IsOldPassword(changePasswordRequest.getOldPassword(), user.getPassword())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(OLD_PASSWORD_MISMATCH));

            } else {
                if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(NEW_PASSWORD_DUPLICATE));
                }
            }
            String encodedNewPassword = encoder.encode(changePasswordRequest.getNewPassword());
            user.setPassword(encodedNewPassword);
            user = userRepository.save(user);
            return user.getId();
        } else {
            throw ApiException.create(HttpStatus.UNAUTHORIZED).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID));
        }
    }

    @Override
    public Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest) {
        User user = getCurrentLoginUser();

        if (personalProfileEditRequest.getBirthday() != null) {
            if (!TimeUtil.isValidBirthday(personalProfileEditRequest.getBirthday(), EUserRole.STUDENT)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
            }
            user.setBirthday(personalProfileEditRequest.getBirthday());
        }

        if (personalProfileEditRequest.getPhone() != null) {
            if (!StringUtil.isValidVietnameseMobilePhoneNumber(personalProfileEditRequest.getPhone())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
            }
            user.setPhone(personalProfileEditRequest.getPhone());
        }


        if (personalProfileEditRequest.getFullName() != null) {
            user.setFullName(personalProfileEditRequest.getFullName());
        }

        if (personalProfileEditRequest.getAddress() != null) {
            user.setAddress(personalProfileEditRequest.getAddress());
        }

        if (personalProfileEditRequest.getGender() != null) {
            user.setGender(personalProfileEditRequest.getGender());
        }


        return userRepository.save(user).getId();
    }


    @Override
    public Long editMentorPersonalProfile(MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest) {
        User user = getCurrentLoginUser();

        MentorUtil.checkMentorStatusToUpdateInformation(user.getMentorProfile());
        if (mentorPersonalProfileEditRequest.getBirthday() != null) {
            if (!TimeUtil.isValidBirthday(mentorPersonalProfileEditRequest.getBirthday(), EUserRole.TEACHER)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
            }
            user.setBirthday(mentorPersonalProfileEditRequest.getBirthday());
        }

        if (mentorPersonalProfileEditRequest.getPhone() != null) {
            if (!StringUtil.isValidVietnameseMobilePhoneNumber(mentorPersonalProfileEditRequest.getPhone())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
            }
            user.setPhone(mentorPersonalProfileEditRequest.getPhone());
        }


        if (mentorPersonalProfileEditRequest.getFullName() != null) {
            user.setFullName(mentorPersonalProfileEditRequest.getFullName());
        }

        if (mentorPersonalProfileEditRequest.getAddress() != null) {
            user.setAddress(mentorPersonalProfileEditRequest.getAddress());
        }

        if (mentorPersonalProfileEditRequest.getGender() != null) {
            user.setGender(mentorPersonalProfileEditRequest.getGender());
        }


        return userRepository.save(user).getId();
    }


    public Long registerAccount(CreateAccountRequest createAccountRequest) {
        validateCreateAccountRequest(createAccountRequest);
        User user = new User();
        if (Boolean.TRUE.equals(userRepository.existsByEmail(createAccountRequest.getEmail()))) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(REGISTERED_EMAIL) + createAccountRequest.getEmail());
        }

        if (Boolean.TRUE.equals(userRepository.existsByPhone(createAccountRequest.getPhone()))) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(REGISTERED_PHONE_NUMBER) + createAccountRequest.getPhone());
        }
        Role role = roleRepository.findRoleByCode(createAccountRequest.getRole()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ROLE_NOT_FOUND_BY_CODE) + createAccountRequest.getRole()));
        if (!TimeUtil.isValidBirthday(createAccountRequest.getBirthDay(), createAccountRequest.getRole())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_BIRTH_DAY));
        }
        user.setEmail(createAccountRequest.getEmail());
        user.setPhone(createAccountRequest.getPhone());
        user.setFullName(createAccountRequest.getFullName());
        user.setGender(createAccountRequest.getGender());
        user.setPassword(encoder.encode(createAccountRequest.getPassword()));
        user.getRoles().add(role);
        user.setBirthday(createAccountRequest.getBirthDay());
        user.setIsVerified(false);
        if (role.getCode().equals(EUserRole.TEACHER)) {
            user.setStatus(false);
            MentorProfile mentorProfile = new MentorProfile();
            mentorProfile.setUser(user);
            mentorProfile.setStatus(EMentorProfileStatus.REQUESTING);
            mentorProfileRepository.save(mentorProfile);
        } else {
            user.setStatus(true);
        }
        User savedUser = userRepository.save(user);
        // Send verify mail
        emailUtil.sendVerifyEmailTo(savedUser);
        Notification.NotificationBuilder builder = Notification.getBuilder();
        Notification notification = builder
                .viTitle("Đăng kí thành công")
                .viContent("Chúc mừng bạn đã đăng ký tài khoản thành công")
                .notifiers(savedUser).build();
        notification = notificationRepository.save(notification);
        ResponseMessage responseMessage = ConvertUtil.convertNotificationToResponseMessage(notification);
        webSocketUtil.sendPrivateNotification(createAccountRequest.getEmail(), responseMessage);
        return savedUser.getId();
    }

    public void validateCreateAccountRequest(CreateAccountRequest request) {
        if (StringUtil.isNullOrEmpty(request.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_EMAIL));
        }
        if (!StringUtil.isValidEmailAddress(request.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_EMAIL));
        }
        if (!StringUtil.isValidVietnameseMobilePhoneNumber(request.getPhone())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
        }
        if (StringUtil.isNullOrEmpty(request.getFullName())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_FULL_NAME));
        }
        boolean isValidGender = request.getGender().equals(EGenderType.MALE) || request.getGender().equals(EGenderType.FEMALE);
        if (!isValidGender) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_GENDER));
        }
        if (!PasswordUtil.validationPassword(request.getPassword())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PASSWORD));
        }
    }

    @Override
    public VerifyResponse verifyAccount(String code) {
        Verification verification = verificationRepository.findByCode(code).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(VERIFY_CODE_NOT_FOUND_BY_CODE) + code));
        User user = verification.getUser();
        Instant createdDate = verification.getCreated();
        EVerifyStatus status = null;
        if (verification.getIsUsed()) { // Verification phải chưa được sử dụng
            status = EVerifyStatus.USED;
        } else if (!TimeUtil.isLessThanHourDurationOfNow(createdDate, 24)) {  // Thời gian xác thực tối đa là 1 ngày
            status = EVerifyStatus.EXPIRED;
        } else {
            user.setIsVerified(true);
            verification.setIsUsed(true);
            status = EVerifyStatus.SUCCESS;
        }
        return VerifyResponse.Builder.getBuilder().withMessage(status.getMessage()).withStatus(status.name()).build().getObject();
    }

    @Override
    public Boolean resendVerifyEmail() {
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser.getIsVerified()) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(VERIFIED_ACCOUNT));
        } else {
            emailUtil.sendVerifyEmailTo(currentUser);
        }
        return true;
    }

    @Override
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(NAME_NOT_FOUND_FROM_OAUTH2_PROVIDER));
        } else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(EMAIL_NOT_FOUND_FROM_OAUTH2_PROVIDER));
        }
        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
        User user = getUserByEmail(oAuth2UserInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(String.format(messageUtil.getLocalMessage(INCORRECT_PROVIDER_LOGIN), user.getProvider(), user.getProvider()));
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setEmail(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addDisplayName(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail()).addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("changeit").build();
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID) + email));
    }

    @Override
    public User registerNewUser(final SignUpRequest signUpRequest) {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(REGISTERED_USER_ID) + signUpRequest.getUserID());
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(REGISTERED_EMAIL) + signUpRequest.getEmail());
        }
        User user = buildUser(signUpRequest);
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setEmail(formDTO.getEmail());
        user.setPassword(encoder.encode(formDTO.getPassword()));
        final List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByCode(EUserRole.STUDENT).get());
        user.setRoles(roles);
        user.setProvider(formDTO.getSocialProvider().getProviderType());
        user.setStatus(true);
//        user.setProviderUserId(formDTO.getProviderUserId());
        return user;
    }
}



