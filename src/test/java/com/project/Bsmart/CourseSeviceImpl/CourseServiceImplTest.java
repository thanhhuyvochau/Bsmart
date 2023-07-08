package com.project.Bsmart.CourseSeviceImpl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.service.Impl.CourseServiceImpl;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MessageUtil messageUtil;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMentorCreateCourse() {
        // giả lập đối tượng SecurityUtil.getCurrentUser()
        User currentUserAccountLogin = mock(User.class);
        when(SecurityUtil.getCurrentUser()).thenReturn(currentUserAccountLogin);

        // giả lập các đối tượng cần thiết
        Long categoryId = 1L;
        Category category = mock(Category.class);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Long subjectId = 1L;
        Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(subjectId);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);
        when(category.getSubjects()).thenReturn((Set<Subject>) subjects);

        MentorProfile mentorProfile = mock(MentorProfile.class);
        when(currentUserAccountLogin.getMentorProfile()).thenReturn(mentorProfile);

        MentorSkill mentorSkill = mock(MentorSkill.class);
        Subject mentorSubject = mock(Subject.class);
        when(mentorSkill.getSkill()).thenReturn(mentorSubject);
        List<MentorSkill> mentorSkills = new ArrayList<>();
        mentorSkills.add(mentorSkill);
        when(mentorProfile.getSkills()).thenReturn(mentorSkills);

        // test case
        CreateCourseRequest createCourseRequest = new CreateCourseRequest();
        createCourseRequest.setCategoryId(categoryId);
        createCourseRequest.setSubjectId(subjectId);
        createCourseRequest.setName("Test Course");
        createCourseRequest.setDescription("Test Course Description");

        when(courseRepository.save(Mockito.any(Course.class))).thenReturn(new Course());

        Long result = courseService.mentorCreateCourse(createCourseRequest);

        assertEquals(result, 1L);
    }
}
