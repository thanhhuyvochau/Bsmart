package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.response.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertUtil {


    public static CategoryDto convertCategoryToCategoryDto(Category category) {
        return ObjectUtil.copyProperties(category, new CategoryDto(), CategoryDto.class);
    }

    public static SubjectDto convertSubjectToSubjectDto(Subject subject) {
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class);
        if (subject.getCategory() != null) {
            subjectDto.setCategoryId(subject.getCategory().getId());
        }
        return subjectDto;
    }


    public static SectionDto convertSectionToSectionDto(Section section) {
        SectionDto sectionDto = ObjectUtil.copyProperties(section, new SectionDto(), SectionDto.class);
        if (section.getCourse() != null) {
            sectionDto.setCourseId(section.getCourse().getId());
        }
        if (!section.getModules().isEmpty()) {
            List<ModuleDto> moduleDtoList = new ArrayList<>();
            for (Module module : section.getModules()) {
                moduleDtoList.add(convertModuleToModuleDto(module));
            }
            sectionDto.setModules(moduleDtoList);
        }
        return sectionDto;
    }


    public static WalletDto convertWalletToWalletDto(Wallet wallet) {
        WalletDto walletDto = ObjectUtil.copyProperties(wallet, new WalletDto(), WalletDto.class);
        if (wallet.getOwner() != null) {
            walletDto.setOwner_id(wallet.getOwner().getId());
        }
        return walletDto;
    }

    public static ImageDto convertImageToImageDto(Image image) {
        ImageDto imageDto = ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class);
        if (image.getUser() != null) {
//            imageDto.setUserId(image.getUser().getId());
        }
        return imageDto;
    }

    public static RoleDto convertRoleToRoleDto(Role role) {
        RoleDto roleDto = ObjectUtil.copyProperties(role, new RoleDto(), RoleDto.class);
        roleDto.setName(role.getCode().getName());
        return roleDto;
    }

    public static SlotDto convertSlotToSlotDto(Slot slot) {
        SlotDto dto = new SlotDto();
        dto.setId(slot.getId());
        dto.setName(slot.getName());
        dto.setCode(slot.getCode());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        return dto;
    }

    public static Slot convertSlotDtoToSlot(SlotDto dto) {
        Slot entity = new Slot();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }


    public static DayOfWeekDTO convertDayOfWeekToDto(DayOfWeek dayOfWeek) {
        return new DayOfWeekDTO(dayOfWeek.getId(), dayOfWeek.getName(), dayOfWeek.getCode());
    }

    public static SimpleClassDto convertClassToSimpleDto(Class clazz) {
        return ObjectUtil.copyProperties(clazz, new SimpleClassDto(), SimpleClassDto.class, true);
    }

    public static TimeInWeekDTO convertTimeInWeekToDto(TimeInWeek timeInWeek) {
        Slot slot = timeInWeek.getSlot();
        DayOfWeek dayOfWeek = timeInWeek.getDayOfWeek();

        DayOfWeekDTO dayOfWeekDTO = convertDayOfWeekToDto(dayOfWeek);
        SlotDto slotDto = convertSlotToSlotDto(slot);


        return new TimeInWeekDTO(dayOfWeekDTO, null, slotDto);
    }


    public static UserDto convertUsertoUserDto(User user) {
        UserDto userDto = ObjectUtil.copyProperties(user, new UserDto(), UserDto.class);
        if (!user.getRoles().isEmpty()) {
            List<RoleDto> roleDtoList = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roleDtoList.add(convertRoleToRoleDto(role));
            }
            userDto.setRoles(roleDtoList);
        }
        if (!user.getUserImages().isEmpty()) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (Image image : user.getUserImages()) {
                imageDtoList.add(convertImageToImageDto(image));
            }
            userDto.setUserImages(imageDtoList);
        }
        if (user.getWallet() != null) {
            userDto.setWallet(convertWalletToWalletDto(user.getWallet()));
        }
        return userDto;
    }


    public static ModuleDto convertModuleToModuleDto(Module module) {
        ModuleDto moduleDto = ObjectUtil.copyProperties(module, new ModuleDto(), ModuleDto.class);
        return moduleDto;
    }



    public static CourseDto convertCourseToCourseDTO(Course course) {
        CourseDto courseDto = ObjectUtil.copyProperties(course, new CourseDto(), CourseDto.class);


//        courseDto.setStatus(course.getStatus());
        if (course.getSubject() != null) {
            courseDto.setSubject(convertSubjectToSubjectDto(course.getSubject()));
        }
        if (course.getMentor() != null) {
            courseDto.setMentorId((course.getMentor().getId()));
        }
//        if (course.getImage() != null) {
//            courseDto.setImage(convertImageToImageDto(course.getImage()));
//        }
//        if (!course.getSections().isEmpty()) {
//            List<SectionDto> sectionDtoList = new ArrayList<>();
//            for (Section section : course.getSections()) {
//                sectionDtoList.add(convertSectionToSectionDto(section));
//            }
//            courseDto.setSections(sectionDtoList);
//        }
//        if (!course.getClasses().isEmpty()) {
//            List<Long> classList = new ArrayList<>();
//            for (Class _class : course.getClasses()) {
//                classList.add(_class.getId());
//            }
//            courseDto.setClasses(classList);
//        }
        return courseDto;
    }

    public static CourseDetailResponse convertCourseToCourseDetailResponse(Course course) {
        CourseDetailResponse response = ObjectUtil.copyProperties(course, new CourseDetailResponse(), CourseDetailResponse.class);

//        response.setStatus(course.getStatus());
        if (course.getSubject() != null) {
            response.setSubject(convertSubjectToSubjectDto(course.getSubject()));
            if (course.getSubject().getCategory() != null) {
                response.setCategoryDto(convertCategoryToCategoryDto(course.getSubject().getCategory()));
            }
        }
        if (course.getMentor() != null) {
            response.setMentorId((course.getMentor().getId()));
        }
//        if (course.getImage() != null) {
//            response.setImage(convertImageToImageDto(course.getImage()));
//        }


        return response;
    }

    public static SubCourseDetailResponse convertSubCourseToSubCourseDetailResponse(SubCourse subCourse) {
        SubCourseDetailResponse subCourseDetailResponse = ObjectUtil.copyProperties(subCourse, new SubCourseDetailResponse(), SubCourseDetailResponse.class);

        subCourseDetailResponse.setImage(ObjectUtil.copyProperties(subCourse.getImage() , new ImageDto() , ImageDto.class));
        List<TimeInWeek> timeInWeeks = subCourse.getTimeInWeeks();

        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>( );
        timeInWeeks.forEach(timeInWeek -> {
            timeInWeekDTOS.add(convertTimeInWeekToDto(timeInWeek) );
        });
        subCourseDetailResponse.setTimeInWeeks(timeInWeekDTOS);
        return subCourseDetailResponse ;
    }

    public static CourseSubCourseDetailResponse convertCourseSubCourseToCourseSubCourseDetailResponse(Course course) {
        CourseSubCourseDetailResponse response = ObjectUtil.copyProperties(course, new CourseSubCourseDetailResponse(), CourseSubCourseDetailResponse.class);



        Subject subject = course.getSubject();
        if (subject != null) {
            response.setSubject(convertSubjectToSubjectDto(subject));
            Category category = subject.getCategory();
            if (category != null) {
                response.setCategory(convertCategoryToCategoryDto(category));
            }
        }
        if (course.getMentor() != null) {
            response.setMentorId(course.getMentor().getId());
        }
//        List<SubCourse> subCourses = course.getSubCourses();
//        List<SubCourseDetailResponse> subCourseDetailResponseList = new ArrayList<>();
//        subCourses.forEach(subCourse -> {
//
//            subCourseDetailResponseList.add(ObjectUtil.copyProperties(subCourse, new SubCourseDetailResponse(), SubCourseDetailResponse.class));
//
//        });
//        response.setSubCourses(subCourseDetailResponseList);
        return response;
    }

    public static CourseSubCourseResponse convertSubCourseToCourseSubCourseResponse(SubCourse subCourse) {
        CourseSubCourseResponse response = new CourseSubCourseResponse();
        Course course = subCourse.getCourse();
        response.setSubCourseId(subCourse.getId());
        response.setCourseId(course.getId());
//        response.setCourseCode(subCourse.getCode());
//        response.setCourseName(subCourse.getName());
//        response.setCourseDescription(subCourse.getDescription());
        response.setTypeLearn(subCourse.getTypeLearn());
//        if (course.getImage() != null) {
//            response.setImageUrl(course.getImage().getUrl());
//        }

        Subject subject = course.getSubject();
        if (subject != null) {
            response.setSubjectName(subject.getName());
            Category category = subject.getCategory();
            if (category != null) {
                response.setCategoryName(category.getName());
            }

            if (course.getMentor() != null) {
                response.setMentorName(course.getMentor().getFullName());
            }


        }
        return response;
    }

    public static CourseResponse convertCourseCourseResponsePage(Course course) {


        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setCourseName(course.getName());
        courseResponse.setCourseCode(course.getCode());
        courseResponse.setCourseDescription(course.getDescription());
        courseResponse.setTotalSubCourse(course.getSubCourses().size());
        List<ETypeLearn> learns = new ArrayList<>();
        List<SubCourse> subCourses = course.getSubCourses();
        List<ImageDto>images = new ArrayList<>( );
        subCourses.forEach(subCourse -> {
            learns.add(subCourse.getTypeLearn());
            if (subCourse.getImage()!=null) {
                images.add(ObjectUtil.copyProperties(subCourse.getImage(), new ImageDto() , ImageDto.class)) ;
            }
        });
        courseResponse.setImages(images);
        courseResponse.setLearns(learns);


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectId(subject.getId());
            courseResponse.setSubjectName(subject.getName());
            Category category = subject.getCategory();
            if (category != null) {
                courseResponse.setCategoryId(category.getId());
                courseResponse.setCategoryName(category.getName());
            }

            if (course.getMentor() != null) {
                courseResponse.setMentorName(course.getMentor().getFullName());
            }


        }
        return courseResponse;
    }

    public static TransactionDto convertTransactionToDto(Transaction transaction) {
        return ObjectUtil.copyProperties(transaction, new TransactionDto(), TransactionDto.class, true);
    }

    public static BankDto convertBankToBankDto(Bank bank) {
        return ObjectUtil.copyProperties(bank, new BankDto(), BankDto.class, true);
    }

    public static MentorProfileDTO convertMentorProfileToMentorProfileDto(MentorProfile mentorProfile) {
        MentorProfileDTO mentorProfileDTO = ObjectUtil.copyProperties(mentorProfile, new MentorProfileDTO(), MentorProfileDTO.class);
        if (mentorProfile.getUser() != null) {
            mentorProfileDTO.setUser(convertUsertoUserDto(mentorProfile.getUser()));
        }
        if (!mentorProfile.getSkills().isEmpty()) {
            List<MentorSkillDto> skillList = new ArrayList<>();
            for(MentorSkill mentorSkill : mentorProfile.getSkills()){
                MentorSkillDto mentorSkillDto = convertMentorSkillToMentorSkillDto(mentorSkill);
                skillList.add(mentorSkillDto);
            }
            mentorProfileDTO.setSkillList(skillList);
        }
        return mentorProfileDTO;
    }

    public static MentorSkillDto convertMentorSkillToMentorSkillDto(MentorSkill mentorSkill){
        MentorSkillDto mentorSkillDto = ObjectUtil.copyProperties(mentorSkill, new MentorSkillDto(), MentorSkillDto.class);
        return mentorSkillDto;
    }

    public static CartResponse convertCartToCartResponse(Cart cart) {
        CartResponse cartResponse = ObjectUtil.copyProperties(cart, new CartResponse(), CartResponse.class, true);
        List<CourseCartResponse> cartItemResponses = cart.getCartItems().stream().map(ConvertUtil::convertCartItemToResponse).collect(Collectors.toList());
        cartResponse.getCartItems().addAll(cartItemResponses);
        return cartResponse;
    }

    public static CourseCartResponse convertCartItemToResponse(CartItem cartItem) {
        SubCourse chooseSubCourse = cartItem.getSubCourse();
        Course course = chooseSubCourse.getCourse();

        CourseCartResponse courseCartResponse = convertCourseToCourseCart(course);
        courseCartResponse.setCartItemId(cartItem.getId());
        for (SubCourse subCourse : course.getSubCourses()) {
            SubCourseCartResponse subCourseCartResponse = ObjectUtil.copyProperties(subCourse, new SubCourseCartResponse(), SubCourseCartResponse.class, true);
            if (subCourseCartResponse.getId().equals(chooseSubCourse.getId())) {
                subCourseCartResponse.setIsChosen(true);
            }
            courseCartResponse.getSubCourses().add(subCourseCartResponse);
        }
        return courseCartResponse;
    }

    private static CourseCartResponse convertCourseToCourseCart(Course course) {
        CourseCartResponse courseCartResponse = ObjectUtil.copyProperties(course, new CourseCartResponse(), CourseCartResponse.class);
        if (course.getSubject() != null) {
            courseCartResponse.setSubject(convertSubjectToSubjectDto(course.getSubject()));
        }
        if (course.getMentor() != null) {
            courseCartResponse.setMentor(convertUsertoUserDto(course.getMentor()));
        }
//        if (course.getImage() != null) {
//            courseCartResponse.setImage(convertImageToImageDto(course.getImage()));
//        }
        return courseCartResponse;
    }
}