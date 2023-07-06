package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.entity.dto.activity.SectionDto;
import fpt.project.bsmart.entity.request.activity.MentorCreateLessonForCourse;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.LessonRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityUtil {
    private static ActivityRepository staticActivityRepository;

    private static LessonRepository staticLessonRepository;

    public ActivityUtil(ActivityRepository activityRepository, LessonRepository lessonRepository) {
        staticActivityRepository = activityRepository;
        staticLessonRepository = lessonRepository;
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
            classDetailResponse.setSections(sections);

        }

    }
}




