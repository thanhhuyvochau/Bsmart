package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassValidator {
    public static boolean isMentorOfClass(User currentUser, Class clazz) {
        User mentor = clazz.getSubCourse().getMentor();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }

    public static Optional<StudentClass> isExistedStudentClass(Class clazz, Long studentClassId) {
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        return studentClasses.stream().filter(studentClass -> Objects.equals(studentClass.getId(), studentClassId)).findFirst();
    }

    public static boolean isUserIsStudentOfClass(Class clazz, User user) {
        List<User> userList = clazz.getStudentClasses().stream().map(StudentClass::getStudent).collect(Collectors.toList());
        return userList.stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()));
    }
}
