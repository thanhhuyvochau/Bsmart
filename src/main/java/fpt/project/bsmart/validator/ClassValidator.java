package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClassValidator {
    public static boolean isMentorOfClass(User currentUser, Class clazz) {
        User mentor = clazz.getSubCourse().getMentor();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }

    public static Optional<StudentClass> isExistedStudentClass(Class clazz, Long studentClassId) {
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        return studentClasses.stream().filter(studentClass -> Objects.equals(studentClass.getId(), studentClassId)).findFirst();
    }
}
