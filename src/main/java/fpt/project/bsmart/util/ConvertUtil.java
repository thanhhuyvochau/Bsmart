//package fpt.project.bsmart.util;
//
//import fpt.project.bsmart.entity.*;
//import fpt.project.bsmart.entity.constant.EGenderType;
//import fpt.project.bsmart.entity.dto.*;
//import fpt.project.bsmart.entity.response.*;
//import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
//import fpt.project.bsmart.moodle.request.GetMoodleCourseRequest;
//import fpt.project.bsmart.moodle.response.MoodleModuleResponse;
//import fpt.project.bsmart.moodle.response.MoodleRecourseDtoResponse;
//import fpt.project.bsmart.moodle.response.MoodleResourceResponse;
//import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Component
//public class ConvertUtil {
//
//
//
//
//    private static MoodleCourseRepository staticMoodleCourseRepository;
//
//    public ConvertUtil(MoodleCourseRepository moodleCourseRepository) {
//        staticMoodleCourseRepository = moodleCourseRepository;
//    }
//
//
//
//
//
//
//
//    public static AccountResponse doConvertEntityToResponse(User user) {
//
//        AccountResponse accountResponse = new AccountResponse();
//        if (user != null) {
//            accountResponse.setUsername(user.getUsername());
//            accountResponse.setId(user.getId());
//
//            RoleDto roleDto = doConvertEntityToResponse(user.getRole());
//            accountResponse.setRole(roleDto);
//            }
//        return accountResponse;
//    }
//
//    public static GenderResponse doConvertEntityToResponse(EGenderType gender) {
//        GenderResponse genderResponse = new GenderResponse();
//        genderResponse.setCode(gender.name());
//        genderResponse.setName(gender.getLabel());
//
//        return genderResponse;
//    }
//
//
//    public static RoleDto doConvertEntityToResponse(Role role) {
//        return ObjectUtil.copyProperties(role, new RoleDto(), RoleDto.class, true);
//    }
//
//
//
//}