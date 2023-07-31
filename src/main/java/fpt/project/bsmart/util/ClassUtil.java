package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetClassDetailResponse;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.repository.StudentClassRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Component
public class ClassUtil {
    private static MessageUtil staticMessageUtil;
    private static StudentClassRepository staticStudentClassRepository;


    public static double CLASS_PERCENTAGE_FOR_FIRST_FEEDBACK = 0.5f;
    public static double CLASS_PERCENTAGE_FOR_SECOND_FEEDBACK = 0.8f;
    public static double PERCENTAGE_RANGE = 0.1f;

    public ClassUtil(MessageUtil messageUtil, StudentClassRepository studentClassRepository) {
        staticStudentClassRepository = studentClassRepository;
        staticMessageUtil = messageUtil;
    }
    /**Get progress for class, particular STARING class*/
    public static ClassProgressTimeDto getPercentageOfClassTime(Class clazz) {
        ClassProgressTimeDto classProgressTimeDto = new ClassProgressTimeDto();
        /**Exit point of function if class is ENDED or not STARING*/
        if (Objects.equals(clazz.getStatus(), ECourseStatus.ENDED)) {
            classProgressTimeDto.setPercentage(100);
            classProgressTimeDto.setCurrentSlot(clazz.getNumberOfSlot());
            return classProgressTimeDto;
        } else if (!Objects.equals(clazz.getStatus(), ECourseStatus.STARTING)) {
            classProgressTimeDto.setPercentage(0);
            classProgressTimeDto.setCurrentSlot(0);
            return classProgressTimeDto;
        }
        /**Get nearest time table that less than or before now*/
        List<TimeTable> timeTables = clazz.getTimeTables();
        Instant now = Instant.now();
        TimeTable nearestTimeTable = null;

        for (TimeTable timeTable : timeTables) {
            Instant timeTableDate = timeTable.getDate();
            if (timeTableDate.isBefore(now) || timeTableDate.equals(now)) {
                if (nearestTimeTable == null || timeTableDate.isAfter(nearestTimeTable.getDate())) {
                    nearestTimeTable = timeTable;
                }
            }
        }
        if (nearestTimeTable != null) {
            Integer currentSlotNum = nearestTimeTable.getCurrentSlotNum();
            classProgressTimeDto.setPercentage(currentSlotNum / clazz.getNumberOfSlot());
            classProgressTimeDto.setCurrentSlot(clazz.getNumberOfSlot());
            return classProgressTimeDto;
        }
        return classProgressTimeDto;
    }

    public static String generateCode(String code) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int codeLength = 4;
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);

            sb.append(randomChar);
        }
        return code + sb.toString();
    }

    public static void checkMentorOfClass(User creator, User currentUserAccountLogin) {
        if (!creator.equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(YOU_DO_NOT_HAVE_PERMISSION_TO_CREATE_CLASS_FOR_THIS_COURSE));
        }

    }

    public static void checkClassStatusToDelete(Class aClass, Course course) {
        if (aClass.getStatus().equals(ECourseStatus.NOTSTART)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(COURSE_STATUS_IS_NOT_START_NOT_ALLOW_TO_DELETE));
        }
        if (aClass.getStatus().equals(ECourseStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(COURSE_STATUS_IS_NOT_STARTING_ALLOW_TO_DELETE));
        }

    }

    public static ClassDetailResponse convertClassToClassDetailResponse(User userLogin, Class clazz) {

        ClassDetailResponse classDetailResponse = ObjectUtil.copyProperties(clazz, new ClassDetailResponse(), ClassDetailResponse.class);
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Optional<StudentClass> byClassAndStudent = staticStudentClassRepository.findByClazzAndStudent(clazz, currentUserAccountLogin);
        if (byClassAndStudent.isPresent()) {
            classDetailResponse.setPurchase(true);
        } else {
            classDetailResponse.setPurchase(false);
        }
        ImageDto imageDto = ConvertUtil.convertClassImageToImageDto(clazz.getClassImage());
        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
        clazz.getTimeInWeeks().forEach(timeInWeek -> {
            timeInWeekDTOS.add(ConvertUtil.convertTimeInWeekToDto(timeInWeek));
        });
        classDetailResponse.setTimeInWeeks(timeInWeekDTOS);
        classDetailResponse.setImage(imageDto);
        if (userLogin != null) {
            List<Order> orders = userLogin.getOrder();
            orders.forEach(order -> {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                orderDetails.forEach(orderDetail -> {
                    Class aClass = orderDetail.getClazz();
                    if (aClass.equals(clazz)) {
                        classDetailResponse.setPurchase(true);
                    }
                });
            });
        }
        ActivityUtil.setSectionForCourse(clazz, classDetailResponse);


        return classDetailResponse;
    }


    public static MentorGetClassDetailResponse convertClassToMentorClassDetailResponse(Class clazz) {
        MentorGetClassDetailResponse classDetailResponse = ObjectUtil.copyProperties(clazz, new MentorGetClassDetailResponse(), MentorGetClassDetailResponse.class);
        if (clazz.getCourse() != null) {
            classDetailResponse.setCourseId(clazz.getCourse().getId());
        }
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        classDetailResponse.setNumberOfStudent(studentClasses.size());
        ImageDto imageDto = ConvertUtil.convertClassImageToImageDto(clazz.getClassImage());
        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
        clazz.getTimeInWeeks().forEach(timeInWeek -> {
            timeInWeekDTOS.add(ConvertUtil.convertTimeInWeekToDto(timeInWeek));
        });
        classDetailResponse.setTimeInWeeks(timeInWeekDTOS);
        classDetailResponse.setImage(imageDto);

        return classDetailResponse;
    }

    public static ManagerGetClassDetailResponse convertClassToManagerGetClassResponse(Class clazz) {
        ManagerGetClassDetailResponse classDetailResponse = ObjectUtil.copyProperties(clazz, new ManagerGetClassDetailResponse(), ManagerGetClassDetailResponse.class);
        classDetailResponse.setNumberOfStudent(clazz.getStudentClasses().size());
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        if (studentClasses != null) {
            List<UserDto> students = studentClasses.stream()
                    .map(StudentClass::getStudent)
                    .map(ConvertUtil::convertUsertoUserDto)
                    .collect(Collectors.toList());
            classDetailResponse.setStudents(students);
        }
        if (clazz.getMentor() != null) {
            classDetailResponse.setMentor(ConvertUtil.convertUsertoUserDto(clazz.getMentor()));
        }
        return classDetailResponse;
    }

    public static BaseClassResponse convertClassToBaseclassResponse(Class clazz) {
        BaseClassResponse classResponse = ObjectUtil.copyProperties(clazz, new BaseClassResponse(), BaseClassResponse.class);
        classResponse.setNumberOfStudent(clazz.getStudentClasses().size());
        return classResponse;
    }

    public static StudentClass findUserInClass(Class clazz, User user) {
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        Optional<StudentClass> optionalStudentClass = studentClasses.stream().filter(studentClass -> Objects.equals(studentClass.getStudent().getId(), user.getId())).findFirst();
        StudentClass studentClass = optionalStudentClass.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy học sinh trong lớp"));
        return studentClass;
    }


}

