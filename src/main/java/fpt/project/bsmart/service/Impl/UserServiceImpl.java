package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.Image;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EImageType;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.repository.ImageRepository;
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

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    private final MessageUtil messageUtil;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final ImageRepository imageRepository;

    public UserServiceImpl(UserRepository userRepository, MessageUtil messageUtil, RoleRepository roleRepository, PasswordEncoder encoder, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.messageUtil = messageUtil;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.imageRepository = imageRepository;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + id));
    }

    private User getCurrentLoginUser() {
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
    public User getUserById(Long id) {
        return findUserById(id);
    }

    @Override
    public Long editUserSocialProfile(SocialProfileEditRequest socialProfileEditRequest) {
        User user = getCurrentLoginUser();
        List<String> errorMessages = new ArrayList<>();

        if (!StringUtil.isValidFacebookLink(socialProfileEditRequest.getFacebookLink())) {
            errorMessages.add("Facebook error message");
        }

        if (!StringUtil.isValidInstagramLink(socialProfileEditRequest.getInstagramLink())) {
            errorMessages.add("Instagram error message");
        }

        if (!StringUtil.isValidTwitterLink(socialProfileEditRequest.getTwitterLink())) {
            errorMessages.add("Twitter error message");
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
            errorMessages.add("Kh??ng ???????c ????? tr???ng m???t kh???u");
        }

        if (!PasswordUtil.validationPassword(accountProfileEditRequest.getNewPassword())) {
            errorMessages.add("Password m???i kh??ng h???p l???");
        }
        String encodedNewPassword = PasswordUtil.BCryptPasswordEncoder(accountProfileEditRequest.getNewPassword());

        if (!PasswordUtil.IsOldPassword(accountProfileEditRequest.getOldPassword(), user.getPassword())) {
            errorMessages.add("M???t kh???u c?? kh??ng tr??ng kh???p");
        } else {
            if (accountProfileEditRequest.getOldPassword().equals(accountProfileEditRequest.getNewPassword())) {
                errorMessages.add("M???t kh???u m???i tr??ng m???t kh???u c??");
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
            errorMessages.add("fullname error message");
        }
        if (StringUtil.isNullOrEmpty(personalProfileEditRequest.getAddress())) {
            errorMessages.add("address error message");
        }
        if (!StringUtil.isValidVietnameseMobilePhoneNumber(personalProfileEditRequest.getPhone())) {
            errorMessages.add("phone number error message");
        }
        if (!DayUtil.isValidBirthday(personalProfileEditRequest.getBirthday())) {
            errorMessages.add("birthday error message");
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
                    .withMessage(messageUtil.getLocalMessage("Email : " + createAccountRequest.getEmail() + "???? ???????c ????ng k??"));
        }

        if (userRepository.existsByPhone(createAccountRequest.getPhone())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("S??? ??i???n tho???i : " + createAccountRequest.getEmail() + "???? ???????c ????ng k??"));
        }


        user.setEmail(createAccountRequest.getEmail());
        user.setPhone(createAccountRequest.getPhone());
        user.setFullName(createAccountRequest.getFullName());
        user.setPassword(encoder.encode(createAccountRequest.getPassword()));
        user.setIntroduce(createAccountRequest.getIntroduce());
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findRoleByCode(createAccountRequest.getRole())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay role"));
        roleList.add(role);
        user.setRoles(roleList);
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




