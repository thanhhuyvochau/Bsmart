package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        Class clazz = timeInWeek.getClazz();
        DayOfWeek dayOfWeek = timeInWeek.getDayOfWeek();

        DayOfWeekDTO dayOfWeekDTO = convertDayOfWeekToDto(dayOfWeek);
        SlotDto slotDto = convertSlotToSlotDto(slot);
        SimpleClassDto simpleClazz = convertClassToSimpleDto(clazz);

        return new TimeInWeekDTO(dayOfWeekDTO, simpleClazz, slotDto);
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
        courseDto.setLevel(course.getLevel());
        courseDto.setCode(course.getCode());
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());
        courseDto.setStatus(course.getStatus());
        if (course.getSubject() != null) {
            courseDto.setSubject(convertSubjectToSubjectDto(course.getSubject()));
        }
        if (course.getMentor() != null) {
            courseDto.setMentorId((course.getMentor().getId()));
        }
        if (course.getImage() != null) {
            courseDto.setImage(convertImageToImageDto(course.getImage()));
        }
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
        response.setLevel(course.getLevel());
        response.setCode(course.getCode());
        response.setName(course.getName());
        response.setDescription(course.getDescription());
        response.setStatus(course.getStatus());
        if (course.getSubject() != null) {
            response.setSubject(convertSubjectToSubjectDto(course.getSubject()));
            if (course.getSubject().getCategory()!= null) {
                response.setCategoryDto(convertCategoryToCategoryDto(course.getSubject().getCategory()));
            }
        }
        if (course.getMentor() != null) {
            response.setMentorId((course.getMentor().getId()));
        }
        if (course.getImage() != null) {
            response.setImage(convertImageToImageDto(course.getImage()));
        }



        return response;
    }

    public static CourseResponse convertCourseCourseResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();
        if (course.getImage() != null) {
            courseResponse.setImageUrl(course.getImage().getUrl());
        }
        courseResponse.setCourseName(course.getName());
        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectName(subject.getName());
            Category category = subject.getCategory();
            if (category != null) {
                courseResponse.setCategoryName(category.getName());
            }
        }
        if (course.getMentor() != null) {
            courseResponse.setMentorName(course.getMentor().getFullName());
        }
        courseResponse.setCourseDescription(course.getDescription());
        return courseResponse;
    }


    public static TransactionDto convertTransactionToDto(Transaction transaction) {
        return ObjectUtil.copyProperties(transaction, new TransactionDto(), TransactionDto.class, true);
    }

    public static BankDto convertBankToBankDto(Bank bank) {
        return ObjectUtil.copyProperties(bank, new BankDto(), BankDto.class, true);
    }

}