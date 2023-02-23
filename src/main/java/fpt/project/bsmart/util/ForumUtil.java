package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.common.EForumType;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForumUtil {
    public static Boolean isValidForumMember(Forum forum, Account account) {
        String name = forum.getType().name();
        EAccountRole roleCode = account.getRole().getCode();
        if (roleCode == null) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("You cannot call this method because your not authority");
        }
        if (name.equals(EForumType.CLASS.name())) {
            Class clazz = forum.getaClazz();
            switch (roleCode) {
                case STUDENT:
                    return isValidClassForStudent(account, clazz);
                case TEACHER:
                    return isValidClassForTeacher(account, clazz);
                default:
                    return true;
            }

        } else if (name.equals(EForumType.SUBJECT.name())) {
            Subject subject = forum.getSubject();
            switch (roleCode) {
                case STUDENT:
                    return isValidSubjectForStudent(account, subject);
                case TEACHER:
                    return isValidSubjectForTeacher(account, subject);
                default:
                    return true;
            }
        }
        return false;
    }

    public static Boolean isValidSubjectForStudent(Account student, Subject subject) {
        if (student == null || subject == null) return false;
        List<Class> enrolledClass = student.getStudentClasses().stream().map(StudentClass::getaClass).collect(Collectors.toList());
        Class classMatchSubject = enrolledClass.stream()
                .filter(aClass -> aClass.getCourse().getSubject() != null)
                .filter(aClass -> aClass.getCourse().getSubject().getId().equals(subject.getId()))
                .findFirst().orElse(null);
        if (classMatchSubject == null) {
            return false;
        }
        return true;
    }

    public static Boolean isValidClassForStudent(Account student, Class clazz) {
        if (student == null || clazz == null) return false;
        long enrolled = clazz.getStudentClasses()
                .stream()
                .filter(studentClass -> studentClass.getAccount().getId().equals(student.getId())).count();
        if (enrolled != 1) {
            return false;
        }
        return true;
    }

    public static Boolean isValidSubjectForTeacher(Account account, Subject subject) {
        if (account == null || subject == null) return false;
        AccountDetail teacherAccountDetail = account.getAccountDetail();
        if (teacherAccountDetail == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Cannot find the profile of this teacher, contact admin for help!");
        }
        AccountDetail result = subject.getAccountDetailSubjects()
                .stream()
                .map(AccountDetailSubject::getAccountDetail)
                .filter(accountDetail -> accountDetail.getId().equals(teacherAccountDetail.getId()))
                .findFirst().orElse(null);
        if (result == null) {
            return false;
        }
        return true;
    }

    public static Boolean isValidClassForTeacher(Account account, Class clazz) {
        if (account == null || clazz == null) return false;
        AccountDetail teacherAccountDetail = account.getAccountDetail();
        if (teacherAccountDetail == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Cannot find the profile of this teacher, contact admin for help!");
        }
        return Objects.equals(clazz.getAccount(), account);
    }
}
