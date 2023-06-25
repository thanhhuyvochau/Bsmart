package fpt.project.bsmart.config.json;

import fpt.project.bsmart.entity.constant.EUserRole;

import java.util.HashMap;
import java.util.Map;

public class View {
    public static class Anonymous {
    }

    public static class Student extends Anonymous {
    }

    public static class Teacher extends Student {
    }

    public static class Manager extends Teacher {
    }

    public static class Admin extends Manager {
    }

    public static final Map<EUserRole, Class> MAPPING = new HashMap<>();

    static {
        MAPPING.put(EUserRole.ANONYMOUS, Anonymous.class);
        MAPPING.put(EUserRole.STUDENT, Student.class);
        MAPPING.put(EUserRole.TEACHER, Teacher.class);
        MAPPING.put(EUserRole.MANAGER, Manager.class);
        MAPPING.put(EUserRole.ADMIN, Admin.class);
    }
}
