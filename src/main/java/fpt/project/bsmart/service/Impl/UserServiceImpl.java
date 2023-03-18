package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EImageType;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.MentorPersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.repository.ImageRepository;
import fpt.project.bsmart.repository.MentorProfileRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IUserService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.*;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    private final MessageUtil messageUtil;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final ImageRepository imageRepository;
    private final MentorProfileRepository mentorProfileRepository;

    public UserServiceImpl(UserRepository userRepository, MessageUtil messageUtil, RoleRepository roleRepository, PasswordEncoder encoder, ImageRepository imageRepository, MentorProfileRepository mentorProfileRepository) {
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.imageRepository = imageRepository;
        this.mentorProfileRepository = mentorProfileRepository;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(USER_NOT_FOUND_BY_ID) + id));
    }

    public User getCurrentLoginUser() {
        return SecurityUtil.getCurrentUserAccountLogin();
    }

    @Override
    public UserDto getLoginUser() {
        return ConvertUtil.convertUsertoUserDto(getCurrentLoginUser());
    }

    @Override
    public Long removeSocialLink(String link) {
        if(StringUtil.isNullOrEmpty(link)){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(INVALID_SOCIAL_LINK);
        }
        User user = getCurrentLoginUser();
        if(user.getFacebookLink().equals(link)){
            user.setFacebookLink(null);
        } else if (user.getInstagramLink().equals(link)) {
            user.setInstagramLink(null);
        } else if (user.getTwitterLink().equals(link)) {
            user.setTwitterLink(null);
        }else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(INVALID_SOCIAL_LINK);
        }
        return userRepository.save(user).getId();
    }

    public Long uploadImageProfile(Long id, UploadImageRequest uploadImageRequest) {
        User user = findUserById(id);
        Image image = new Image();
        String name = uploadImageRequest.getFile().getOriginalFilename() + "-" + Instant.now().toString();
//        ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, uploadImageRequest.getFile().getContentType(),
//                uploadImageRequest.getFile().getInputStream(), uploadImageRequest.getFile().getSize());
        image.setName(name);
//        image.setUrl(RequestUrlUtil.buildUrl(minioUrl, objectWriteResponse));
        image.setUser(user);
        if (uploadImageRequest.getImageType().equals(EImageType.AVATAR)) {
            image.setType(EImageType.AVATAR);
        } else if (uploadImageRequest.getImageType().equals(EImageType.CI)) {
            image.setType(EImageType.CI);
        }
        return imageRepository.save(image).getId();
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto = ConvertUtil.convertUsertoUserDto(findUserById(id));
        userDto.setWallet(null);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    public Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest) {
        User user = getCurrentLoginUser();

        if(StringUtil.isNotNullOrEmpty(socialProfileEditRequest.getFacebookLink())){
            if (!StringUtil.isValidFacebookLink(socialProfileEditRequest.getFacebookLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FACEBOOK_LINK));
            }
            user.setFacebookLink(socialProfileEditRequest.getFacebookLink());
        }
        if(StringUtil.isNotNullOrEmpty(socialProfileEditRequest.getInstagramLink())){
            if (!StringUtil.isValidInstagramLink(socialProfileEditRequest.getInstagramLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_INSTAGRAM_LINK));
            }
            user.setInstagramLink(socialProfileEditRequest.getInstagramLink());
        }
        if(StringUtil.isNotNullOrEmpty(socialProfileEditRequest.getTwitterLink())){
            if (!StringUtil.isValidTwitterLink(socialProfileEditRequest.getTwitterLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_TWITTER_LINK));
            }
            user.setTwitterLink(socialProfileEditRequest.getTwitterLink());
        }
        return userRepository.save(user).getId();
    }

    @Override
    public Long editUserAccountProfile(AccountProfileEditRequest accountProfileEditRequest) {
        User user = getCurrentLoginUser();
        List<String> errorMessages = new ArrayList<>();

        if (accountProfileEditRequest.getOldPassword().isEmpty() ||
                accountProfileEditRequest.getNewPassword().isEmpty()) {

            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(EMPTY_PASSWORD));
        }

        if (!PasswordUtil.validationPassword(accountProfileEditRequest.getNewPassword())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(INVALID_PASSWORD));

        }
        String encodedNewPassword = PasswordUtil.BCryptPasswordEncoder(accountProfileEditRequest.getNewPassword());

        if (!PasswordUtil.IsOldPassword(accountProfileEditRequest.getOldPassword(), user.getPassword())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(OLD_PASSWORD_MISMATCH));

        } else {
            if (accountProfileEditRequest.getOldPassword().equals(accountProfileEditRequest.getNewPassword())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(NEW_PASSWORD_DUPLICATE));
            }
        }
        user.setPassword(encodedNewPassword);
        return userRepository.save(user).getId();
    }

    @Override
    public Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest) {
        User user = getCurrentLoginUser();

        if (personalProfileEditRequest.getBirthday() != null) {
            if (!DayUtil.isValidBirthday(personalProfileEditRequest.getBirthday())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
            }
            user.setBirthday(personalProfileEditRequest.getBirthday());
        }

        if (personalProfileEditRequest.getPhone() != null) {
            if (!StringUtil.isValidVietnameseMobilePhoneNumber(personalProfileEditRequest.getPhone())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
            }
            user.setPhone(personalProfileEditRequest.getPhone());
        }


        if (personalProfileEditRequest.getFullname() != null) {
            user.setFullName(personalProfileEditRequest.getFullname());
        }

        if (personalProfileEditRequest.getAddress() != null) {
            user.setAddress(personalProfileEditRequest.getAddress());
        }


        return userRepository.save(user).getId();
    }


    @Override
    public Long editMentorPersonalProfile(MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest) {
        User user = getCurrentLoginUser();
        if (mentorPersonalProfileEditRequest.getBirthday() != null) {
            if (!DayUtil.isValidBirthday(mentorPersonalProfileEditRequest.getBirthday())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
            }
            user.setBirthday(mentorPersonalProfileEditRequest.getBirthday());
        }

        if (mentorPersonalProfileEditRequest.getPhone() != null) {
            if (!StringUtil.isValidVietnameseMobilePhoneNumber(mentorPersonalProfileEditRequest.getPhone())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
            }
            user.setPhone(mentorPersonalProfileEditRequest.getPhone());
        }


        if (mentorPersonalProfileEditRequest.getFullName() != null) {
            user.setFullName(mentorPersonalProfileEditRequest.getFullName());
        }

        if (mentorPersonalProfileEditRequest.getAddress() != null) {
            user.setAddress(mentorPersonalProfileEditRequest.getAddress());
        }
        if (mentorPersonalProfileEditRequest.getIntroduce()!=null) {
            user.setIntroduce(mentorPersonalProfileEditRequest.getIntroduce());
        }
        return userRepository.save(user).getId();
    }

    public Long registerAccount(CreateAccountRequest createAccountRequest) {

        User user = new User();
        if (userRepository.existsByEmail(createAccountRequest.getEmail())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Email : " + createAccountRequest.getEmail() + "đã được đăng ký"));
        }

        if (userRepository.existsByPhone(createAccountRequest.getPhone())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Số điện thoại : " + createAccountRequest.getEmail() + "đã được đăng ký"));
        }


        user.setEmail(createAccountRequest.getEmail());
        user.setPhone(createAccountRequest.getPhone());
        user.setFullName(createAccountRequest.getFullName());
        user.setPassword(encoder.encode(createAccountRequest.getPassword()));
        user.setIntroduce(createAccountRequest.getIntroduce());
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findRoleByCode(createAccountRequest.getRole())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay role"));
        if (role.getCode().equals(EUserRole.STUDENT)) {
            user.setStatus(true);
        }
        if (role.getCode().equals(EUserRole.TEACHER)) {
            user.setStatus(false);
        }
        roleList.add(role);
        user.setRoles(roleList);
        if (role.getCode().equals(EUserRole.STUDENT)) {
            user.setStatus(true);
        } else if (role.getCode().equals(EUserRole.TEACHER)) {
            user.setStatus(false);
            Long userId = userRepository.save(user).getId();
            MentorProfile mentorProfile = new MentorProfile();
            mentorProfile.setUser(user);
            mentorProfile.setStatus(false);
            mentorProfileRepository.save(mentorProfile);
            return userId;
        }
        return userRepository.save(user).getId();
    }


}




