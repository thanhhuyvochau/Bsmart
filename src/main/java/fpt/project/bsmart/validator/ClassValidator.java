package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.util.ClassUtil;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.util.TextUtil;
import fpt.project.bsmart.util.TimeUtil;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CLASS_NOT_FOUND_BY_ID;

public class ClassValidator {
    public static boolean isMentorOfClass(User currentUser, Class clazz) {
        User mentor = clazz.getCourse().getCreator();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }

    public static Optional<StudentClass> isExistedStudentClass(Class clazz, Long studentClassId) {
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        return studentClasses.stream().filter(studentClass -> Objects.equals(studentClass.getStudent().getId(), studentClassId)).findFirst();
    }

    public static boolean isStudentOfClass(Class clazz, User user) {
        List<User> userList = clazz.getStudentClasses().stream().map(StudentClass::getStudent).collect(Collectors.toList());
        return userList.stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()));
    }

    public static boolean isMemberOfClass(Class clazz, User user) {
        return isStudentOfClass(clazz, user) || isMentorOfClass(user, clazz);
    }

    public static EUserRole isMemberOfClassAsRole(Class clazz, User user) {
        if (isStudentOfClass(clazz, user)) {
            return EUserRole.STUDENT;
        } else if (isMentorOfClass(user, clazz)) {
            return EUserRole.TEACHER;
        }
        return null;
    }

    public static boolean isValidTimeOfClass(List<TimeInWeek> timeInWeeks, Integer numberOfSlot, Instant startDate, Instant endDate) throws Exception {
        Set<EDayOfWeekCode> availableDOW = timeInWeeks.stream()
                .map(timeInWeek -> timeInWeek.getDayOfWeek().getCode())
                .collect(Collectors.toSet());
        startDate = startDate.truncatedTo(ChronoUnit.DAYS);
        endDate = endDate.truncatedTo(ChronoUnit.DAYS);
        if (startDate.isAfter(endDate) || !checkDateToStartAndEndClass(startDate, endDate)) {
            throw new Exception("Ngày kết thúc lớp phải sau ngày bắt đầu ít nhất 15 ngày");
        }
        int i = numberOfSlot;
        while (i > 0 || startDate.isBefore(endDate)) {
            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(startDate);
            if (dayOfWeekCode == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không thể nhận diện thứ trong tuần, lỗi hệ thống vui lòng liên hệ Admin!");
            }
            if (availableDOW.contains(dayOfWeekCode)) {
                int slotInDay = (int) timeInWeeks.stream().filter(timeInWeek -> timeInWeek.getDayOfWeek().getCode().equals(dayOfWeekCode)).count();
                i = i - slotInDay;
            }
            if (startDate.equals(endDate.truncatedTo(ChronoUnit.DAYS)) && i > 0) {
                throw ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Số lượng buổi học và ngày kết thúc không hợp lệ, vui lòng điều chỉnh lại");
            } else if (!startDate.equals(endDate.truncatedTo(ChronoUnit.DAYS)) && i == 0) {
                throw ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Số lượng buổi học và ngày kết thúc không hợp lệ, vui lòng điều chỉnh lại");
            }
            startDate = startDate.plus(1, ChronoUnit.DAYS);
        }
        return true;
    }

    public static boolean checkDateToStartAndEndClass(Instant startDate, Instant endDate) throws Exception {
        Duration duration = Duration.between(startDate, endDate);
        if (duration.toDays() < 15) {
            return false;
        }
        return true;
    }

    public static boolean checkAllowMeetingLink(String meetingURL, List<String> allowedMeetingURLs) {
        String baseUrl = TextUtil.extractBaseUrl(meetingURL);
        return allowedMeetingURLs.stream().anyMatch(allowedMeetingURL -> allowedMeetingURL.equalsIgnoreCase(baseUrl));
    }

    public static List<BaseClassResponse> getDuplicateTimeMentorClass(Class clazz, List<Class> existedOfOperatingClasses) {
        User student = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Instant startDate = clazz.getStartDate();

        existedOfOperatingClasses = existedOfOperatingClasses.stream()
                .filter(existedOfOperatingClass -> !startDate.isBefore(existedOfOperatingClass.getStartDate()) && !startDate.isAfter(existedOfOperatingClass.getEndDate()))
                .collect(Collectors.toList());


        Set<EDayOfWeekCode> checkedClassEDayCodes = clazz.getTimeInWeeks()
                .stream()
                .map(TimeInWeek::getDayOfWeek)
                .map(DayOfWeek::getCode)
                .collect(Collectors.toSet());

        List<Class> duplicateClasses = existedOfOperatingClasses.stream()
                .filter(possibleDuplicateClass -> possibleDuplicateClass.getTimeInWeeks()
                        .stream()
                        .anyMatch(timeInWeek -> checkedClassEDayCodes.contains(timeInWeek.getDayOfWeek().getCode())))
                .filter(possibleDuplicateClass -> possibleDuplicateClass.getTimeInWeeks()
                        .stream()
                        .anyMatch(timeInWeek -> clazz.getTimeInWeeks().stream()
                                .anyMatch(checkedTimeInWeek -> checkedTimeInWeek.getDayOfWeek().getCode().equals(timeInWeek.getDayOfWeek().getCode())
                                        && checkedTimeInWeek.getSlot().getId().equals(timeInWeek.getSlot().getId()))))
                .collect(Collectors.toList());

        return duplicateClasses.stream()
                .map(ClassUtil::convertClassToBaseclassResponse)
                .collect(Collectors.toList());
    }
}
