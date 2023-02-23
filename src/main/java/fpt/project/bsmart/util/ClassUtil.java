package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.EAccountRole;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ClassUtil {
    public static Boolean isValidClassMember(Class clazz, Account account) {
        Role role = account.getRole();
        EAccountRole roleCode = role.getCode();
        switch (roleCode) {
            case STUDENT:
                return isValidClassForStudent(account, clazz);
            case TEACHER:
                return isValidClassForTeacher(account, clazz);
            default:
                return true;
        }

    }

    private static Boolean isValidClassForStudent(Account student, Class clazz) {
        if (student == null || clazz == null) return false;
        long enrolled = clazz.getStudentClasses()
                .stream()
                .filter(studentClass -> studentClass.getAccount().getId().equals(student.getId())).count();
        if (enrolled != 1) {
            return false;
        }
        return true;
    }

    private static Boolean isValidClassForTeacher(Account account, Class clazz) {
        if (account == null || clazz == null) return false;
        AccountDetail teacherAccountDetail = account.getAccountDetail();
        if (teacherAccountDetail == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Cannot find the profile of this teacher, contact admin for help!");
        }
        return Objects.equals(clazz.getAccount(), account);
    }
}
