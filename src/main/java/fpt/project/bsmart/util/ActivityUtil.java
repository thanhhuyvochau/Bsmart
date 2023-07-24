package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.activity.SectionDto;
import fpt.project.bsmart.entity.request.activity.LessonDto;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetLessonForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Component
public class ActivityUtil {
    private static ActivityRepository staticActivityRepository;

    private static LessonRepository staticLessonRepository;

    private static MessageUtil staticMessageUtil;

    public ActivityUtil(ActivityRepository activityRepository, LessonRepository lessonRepository, MessageUtil messageUtil) {
        staticActivityRepository = activityRepository;
        staticLessonRepository = lessonRepository;
        staticMessageUtil = messageUtil;
    }

    public static void setSectionForCourse(Class clazz, ClassDetailResponse classDetailResponse) {

        Course course = clazz.getCourse();
        List<Activity> byCourseAndParentIdIsNull = staticActivityRepository.findByCourseAndParentIdIsNull(course);

        if (byCourseAndParentIdIsNull != null) {
            List<SectionDto> sections = new ArrayList<>();
            byCourseAndParentIdIsNull.forEach(activity -> {
                SectionDto sectionDto = new SectionDto();
                sectionDto.setName(activity.getName());
                sections.add(sectionDto);

                List<LessonDto> lessons = new ArrayList<>();
                List<Activity> ActivityLessonsDb = staticActivityRepository.findByCourseAndParentId(course, activity.getId());
                List<Long> idLessons = ActivityLessonsDb.stream().map(Activity::getId).collect(Collectors.toList());
                List<Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
                if (lessonDbs != null) {
                    lessonDbs.forEach(lesson -> {
                        LessonDto lessonForCourse = new LessonDto();
                        lessonForCourse.setDescription(lesson.getDescription());
                        lessons.add(lessonForCourse);
                    });
//                    sectionDto.setLessons(lessons);
                }

            });
//            classDetailResponse.setSections(sections);

        }

    }

    public static List<MentorGetSectionForCourse> mentorGetSectionOfCourse(Course course) {
        List<MentorGetSectionForCourse> sectionOfCourses = new ArrayList<>();


        List<Activity> byCourseAndParentIdIsNull = staticActivityRepository.findByCourseAndParentIdIsNull(course);

        if (byCourseAndParentIdIsNull != null) {

            byCourseAndParentIdIsNull.forEach(activity -> {
                MentorGetSectionForCourse response = new MentorGetSectionForCourse();
                response.setId(activity.getId());
                response.setName(activity.getName());


                List<MentorGetLessonForCourse> lessons = new ArrayList<>();
                List<Activity> ActivityLessonsDb = staticActivityRepository.findByCourseAndParentId(course, activity.getId());
                List<Long> idLessons = ActivityLessonsDb.stream().map(Activity::getId).collect(Collectors.toList());
                List<fpt.project.bsmart.entity.Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
                if (lessonDbs != null) {
                    lessonDbs.forEach(lesson -> {
                        MentorGetLessonForCourse lessonForCourse = new MentorGetLessonForCourse();
                        lessonForCourse.setId(lesson.getId());
                        lessonForCourse.setDescription(lesson.getDescription());
                        lessons.add(lessonForCourse);
                    });
                    response.setLessons(lessons);
                }
                sectionOfCourses.add(response);
            });


        }
        return sectionOfCourses;
    }

    public static void checkCourseIsAllowedUpdateOrDelete(Course course) {
        if (!course.getStatus().equals(ECourseStatus.REQUESTING) && !course.getStatus().equals(ECourseStatus.EDITREQUEST)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }
        List<Class> classesOfCourse = course.getClasses().stream().filter(aClass -> aClass.getStatus().equals(ECourseStatus.WAITING) || aClass.getStatus().equals(ECourseStatus.STARTING)).collect(Collectors.toList());
        if (classesOfCourse.size() > 0) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(CLASSES_ARE_CURRENTLY_STARTING_FROM_THIS_COURSE_CANNOT_UPDATE));
        }
    }

    public static List<SectionDto> GetSectionOfCoursePage(Course course) {
        List<SectionDto> sectionDtoList = new ArrayList<>();

        List<Activity> byCourseAndParentIdIsNull = staticActivityRepository.findByCourseAndParentIdIsNull(course);

        if (byCourseAndParentIdIsNull != null) {

            byCourseAndParentIdIsNull.forEach(activity -> {
                SectionDto sectionDto = new SectionDto();


                sectionDto.setName(activity.getName());

                List<LessonDto> lessons = new ArrayList<>();

                List<Activity> ActivityLessonsDb = staticActivityRepository.findByCourseAndParentId(course, activity.getId());
                List<Long> idLessons = ActivityLessonsDb.stream().map(Activity::getId).collect(Collectors.toList());
                List<fpt.project.bsmart.entity.Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
                if (lessonDbs != null) {
                    lessonDbs.forEach(lesson -> {
                        LessonDto lessonForCourse = new LessonDto();
                        lessonForCourse.setDescription(lesson.getDescription());
                        lessons.add(lessonForCourse);
                    });

//                    sectionDto.setLessons(lessons);
                }
                sectionDtoList.add(sectionDto);
            });


        }
        return sectionDtoList;
    }

    public static boolean isCorrectActivityType(Activity activity, ECourseActivityType type){
        switch (type){
            case QUIZ:
                return activity.getQuiz() != null;
            case ANNOUNCEMENT:
                return activity.getAnnouncement() != null;
            case RESOURCE:
                return activity.getResource() != null;
            case ASSIGNMENT:
                return activity.getAssignment() != null;
            case LESSON:
                return activity.getLesson() != null;
            case SECTION:
                return activity != null;
        }
        return false;
    }

    public static boolean haveAuthorizeToView(Activity activity, Class clazz) {
        long isAuthorized = activity.getActivityAuthorizes().stream().filter(activityAuthorize -> {
            Class authorizeClass = activityAuthorize.getAuthorizeClass();
            return Objects.equals(authorizeClass.getId(), clazz.getId());
        }).count();
        return isAuthorized > 0;
    }

    public static boolean isBelongToMentor(Activity activity){
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        boolean isMentor = SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER);
        if(!isMentor){
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(staticMessageUtil.getLocalMessage(FORBIDDEN));
        }
        boolean isBelongToMentor = Objects.equals(user, activity.getCourse().getCreator());
        return isBelongToMentor;
    }
}




