package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.EUserRole;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
}
