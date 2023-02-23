package fpt.project.bsmart.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.response.MarkResponse;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.request.GetCourseGradeRequest;
import fpt.project.bsmart.moodle.response.CourseGradeResponse;
import fpt.project.bsmart.moodle.response.GradeItemResponse;
import fpt.project.bsmart.moodle.response.UserGradeResponse;
import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.MarkRepository;
import fpt.project.bsmart.repository.ModuleRepository;
import fpt.project.bsmart.service.IMarkService;
import fpt.project.bsmart.util.ClassUtil;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MarkServiceImpl implements IMarkService {
    private final MarkRepository markRepository;
    private final AccountRepository accountRepository;
    private final ClassRepository classRepository;
    private final MessageUtil messageUtil;
    private final SecurityUtil securityUtil;
    private final MoodleCourseRepository moodleCourseRepository;
    private final ModuleRepository moduleRepository;

    public MarkServiceImpl(MarkRepository markRepository, AccountRepository accountRepository, ClassRepository classRepository, MessageUtil messageUtil, SecurityUtil securityUtil, MoodleCourseRepository moodleCourseRepository, ModuleRepository moduleRepository) {
        this.markRepository = markRepository;
        this.accountRepository = accountRepository;
        this.classRepository = classRepository;
        this.messageUtil = messageUtil;
        this.securityUtil = securityUtil;
        this.moodleCourseRepository = moodleCourseRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<MarkResponse> getStudentMark(Long clazzId, Long studentId) {
        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found by id:" + clazzId));
        Account student = accountRepository.findById(studentId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Student not found by id:" + studentId));
        Account currentUser = securityUtil.getCurrentUserThrowNotFoundException();
        Boolean isValidMember = ClassUtil.isValidClassMember(clazz, student);
        if (!isValidMember)
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Class is not valid to you!");
        Role role = currentUser.getRole();
        EAccountRole roleCode = role.getCode();
        boolean isValidToGetMark = false;
        switch (roleCode) {
            case STUDENT:
                if (Objects.equals(currentUser.getId(), student.getId())) {
                    isValidToGetMark = true;
                }
                break;
            case MANAGER:
            case TEACHER:
                isValidToGetMark = true;
                break;
        }
        if (isValidToGetMark) {
            List<Mark> studentMarks = markRepository.findAllByStudent_AccountAndModule_Section_Clazz(student, clazz);
            return ConvertUtil.doConvertEntityToListResponse(studentMarks);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean synchronizeMark() throws JsonProcessingException {
        Map<Integer, Module> moduleMap = moduleRepository.findAll().stream()
                .filter(module -> module.getMoodleId() != null)
                .collect(Collectors.toMap(Module::getMoodleId, Function.identity()));

        List<Class> classes = classRepository.findAllByStatus(EClassStatus.STARTING);
        /** Dữ liệu để test, do dữ liệu hiện tại bị lỗi*/
//        List<Class> classes = new ArrayList<>();
//        classes.add(classRepository.findByMoodleClassId(26L));

        for (Class clazz : classes) {
            Map<Integer, StudentClass> studentClassMap = clazz.getStudentClasses()
                    .stream()
                    .collect(Collectors.toMap(studentClass -> studentClass.getAccount().getMoodleUserId(), Function.identity()));

            GetCourseGradeRequest getCourseGradeRequest = new GetCourseGradeRequest();
            getCourseGradeRequest.setCourseid(clazz.getMoodleClassId());
            CourseGradeResponse courseGrade = moodleCourseRepository.getCourseGrade(getCourseGradeRequest);

            for (UserGradeResponse userGrade : courseGrade.getUsergrades()) {
                StudentClass studentClass = Optional.ofNullable(studentClassMap.get(userGrade.getUserid()))
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Student in Moodle not exsit in Vuon Dau, contact admin to help!"));
                /** Dữ liệu để test, do dữ liệu hiện tại bị lỗi*/
//                StudentClass studentClass = studentClassMap.get(userGrade.getUserid());
                if (studentClass == null) {
                    continue;
                }
                List<Mark> marks = createMarks(userGrade, studentClass, moduleMap);
                studentClass.getMarks().clear();
                studentClass.getMarks().addAll(marks);
            }
        }
        classRepository.saveAll(classes);
        return true;
    }

    private List<Mark> createMarks(UserGradeResponse userGrade, StudentClass studentClass, Map<Integer, Module> moduleMap) {
        List<Mark> marks = new ArrayList<>();
        for (GradeItemResponse gradeItem : userGrade.getGradeitems()) {
            Module module = moduleMap.get(gradeItem.getCmid());
            if (module != null && gradeItem.getGraderaw() != null) {
                Mark mark = new Mark();
                mark.setMark((float) gradeItem.getGraderaw());
                mark.setMaxMark((float) gradeItem.getGrademax());
                mark.setMinMark((float) gradeItem.getGrademin());
                mark.setModule(module);
                mark.setStudent(studentClass);
                marks.add(mark);
            }
        }
        return marks;
    }

}
