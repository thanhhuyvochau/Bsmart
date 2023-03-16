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

    @Override
    public User getCurrentLoginUser() {
        return SecurityUtil.getCurrentUserAccountLogin();
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
        return ConvertUtil.convertUsertoUserDto(findUserById(id));
    }

    @Override
    public Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest) {
        User user = getCurrentLoginUser();
        List<String> errorMessages = new ArrayList<>();

        if (!StringUtil.isValidFacebookLink(socialProfileEditRequest.getFacebookLink())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_FACEBOOK_LINK));
        }

        if (!StringUtil.isValidInstagramLink(socialProfileEditRequest.getInstagramLink())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_INSTAGRAM_LINK));
        }

        if (!StringUtil.isValidTwitterLink(socialProfileEditRequest.getTwitterLink())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_TWITTER_LINK));
        }

        if (!errorMessages.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(String.join(", ", errorMessages));
        }

        user.setFacebookLink(socialProfileEditRequest.getFacebookLink());
        user.setInstagramLink(socialProfileEditRequest.getInstagramLink());
        user.setTwitterLink(socialProfileEditRequest.getTwitterLink());

        return userRepository.save(user).getId();
    }

    @Override
    public Long editUserAccountProfile(AccountProfileEditRequest accountProfileEditRequest) {
        User user = getCurrentLoginUser();
        List<String> errorMessages = new ArrayList<>();

        if (StringUtil.isNullOrEmpty(accountProfileEditRequest.getOldPassword())
                || StringUtil.isNullOrEmpty(accountProfileEditRequest.getNewPassword())) {
            errorMessages.add(messageUtil.getLocalMessage(EMPTY_PASSWORD));
        }

        if (!PasswordUtil.validationPassword(accountProfileEditRequest.getNewPassword())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_PASSWORD));
        }
        String encodedNewPassword = PasswordUtil.BCryptPasswordEncoder(accountProfileEditRequest.getNewPassword());

        if (!PasswordUtil.IsOldPassword(accountProfileEditRequest.getOldPassword(), user.getPassword())) {
            errorMessages.add(messageUtil.getLocalMessage(OLD_PASSWORD_MISMATCH));
        } else {
            if (accountProfileEditRequest.getOldPassword().equals(accountProfileEditRequest.getNewPassword())) {
                errorMessages.add(messageUtil.getLocalMessage(NEW_PASSWORD_DUPLICATE));
            }
        }

        if (!errorMessages.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(String.join(", ", errorMessages));
        }
        user.setPassword(encodedNewPassword);
        return userRepository.save(user).getId();
    }

    @Override
    public Long editUserPersonalProfile(PersonalProfileEditRequest personalProfileEditRequest) {
        User user = getCurrentLoginUser();
        List<String> errorMessages = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(personalProfileEditRequest.getFullname())) {
            errorMessages.add(messageUtil.getLocalMessage(EMPTY_FULL_NAME));
        }
        if (StringUtil.isNullOrEmpty(personalProfileEditRequest.getAddress())) {
            errorMessages.add(messageUtil.getLocalMessage(EMPTY_ADDRESS));
        }
        if (!StringUtil.isValidVietnameseMobilePhoneNumber(personalProfileEditRequest.getPhone())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
        }
        if (!DayUtil.isValidBirthday(personalProfileEditRequest.getBirthday())) {
            errorMessages.add(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
        }
        if (!errorMessages.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(String.join(", ", errorMessages));
        }
        user.setFullName(personalProfileEditRequest.getFullname());
        user.setAddress(personalProfileEditRequest.getAddress());
        user.setAddress(personalProfileEditRequest.getAddress());
        user.setBirthday(personalProfileEditRequest.getBirthday());
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
        if (role.getCode().equals(EUserRole.STUDENT)){
            user.setStatus(true);
        }
        if (role.getCode().equals(EUserRole.TEACHER)){
            user.setStatus(false);
        }
        roleList.add(role);
        user.setRoles(roleList);
        if(role.getCode().equals(EUserRole.STUDENT)){
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
//    @Override
//    public Long saveUser(CreateAccountRequest createAccountRequest) {
//        User user = new User();
//        user.setUsername(createAccountRequest.getUsername());
//        user.setPassword(bCryptEncoder.encode(createAccountRequest.getPassword()));
//        user.setEmail(createAccountRequest.getEmail());
//        user.setAddress(createAccountRequest.getAddress());
//        user.setBirthday(createAccountRequest.getBirthday());
//        user.setPhone(createAccountRequest.getPhone());
//        user.setFullName(createAccountRequest.getFullName());
//        List<Role> roleList = new ArrayList<>();
//        Role role = roleRepo.findRoleByCode(createAccountRequest.getRole())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay role"));
//        roleList.add(role);
//        user.setRoles(roleList);
//
//        return userRepo.save(user).getId();
//    }

//    @Override
//    public Optional<User> findByUsername(String username) {
//        return userRepo.findByUsername(username);
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> opt = userRepo.findByUsername(username);
//        org.springframework.security.core.userdetails.User springUser = null;
//        if (!opt.isPresent()) {
//            throw new UsernameNotFoundException("User with username: " + username + " not found");
//        } else {
//            User user = opt.get();    //retrieving user from DB
//            List<Role> roles = user.getRoles();
//            Set<GrantedAuthority> ga = new HashSet<>();
//            for (Role role : roles) {
//                ga.add(new SimpleGrantedAuthority(role.getCode().toString()));
//            }
//
//            springUser = new org.springframework.security.core.userdetails.User(
//                    username,
//                    user.getPassword(),
//                    ga);
//        }
//
//        return springUser;
//    }
}




