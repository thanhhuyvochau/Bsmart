package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.activity.SectionDto;
import fpt.project.bsmart.entity.request.activity.MentorCreateLessonForCourse;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetLessonForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

                List<MentorCreateLessonForCourse> lessons = new ArrayList<>();
                List<Activity> ActivityLessonsDb = staticActivityRepository.findByCourseAndParentId(course, activity.getId());
                List<Long> idLessons = ActivityLessonsDb.stream().map(Activity::getId).collect(Collectors.toList());
                List<Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
                if (lessonDbs != null) {
                    lessonDbs.forEach(lesson -> {
                        MentorCreateLessonForCourse lessonForCourse = new MentorCreateLessonForCourse();
                        lessonForCourse.setDescription(lesson.getDescription());
                        lessons.add(lessonForCourse);
                    });
                    sectionDto.setLessons(lessons);
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
                List<Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
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

                List<MentorCreateLessonForCourse> lessons = new ArrayList<>();

                List<Activity> ActivityLessonsDb = staticActivityRepository.findByCourseAndParentId(course, activity.getId());
                List<Long> idLessons = ActivityLessonsDb.stream().map(Activity::getId).collect(Collectors.toList());
                List<Lesson> lessonDbs = staticLessonRepository.findByActivityIdIn(idLessons);
                if (lessonDbs != null) {
                    lessonDbs.forEach(lesson -> {
                        MentorCreateLessonForCourse lessonForCourse = new MentorCreateLessonForCourse();
                        lessonForCourse.setDescription(lesson.getDescription());
                        lessons.add(lessonForCourse);
                    });

                    sectionDto.setLessons(lessons);
                }
                sectionDtoList.add(sectionDto);
            });


        }
        return sectionDtoList;
    }
}




