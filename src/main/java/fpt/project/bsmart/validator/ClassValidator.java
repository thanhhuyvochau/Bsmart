package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;
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

public class ClassValidator {
    public static boolean isMentorOfClass(User currentUser, Class clazz) {
        User mentor = clazz.getCourse().getCreator();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }

    public static Optional<StudentClass> isExistedStudentClass(Class clazz, Long studentClassId) {
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        return studentClasses.stream().filter(studentClass -> Objects.equals(studentClass.getId(), studentClassId)).findFirst();
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
}
