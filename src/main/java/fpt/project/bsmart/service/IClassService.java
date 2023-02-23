package fpt.project.bsmart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.dto.ClassDetailDto;
import fpt.project.bsmart.entity.dto.ClassDto;
import fpt.project.bsmart.entity.dto.ClassStudentDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.*;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface IClassService {


    Long teacherRequestCreateClass(TeacherCreateClassRequest createClassRequest) throws JsonProcessingException, ParseException;

    Long teacherSubmitRequestCreateClass(Long id) throws JsonProcessingException;

    Long teacherRequestCreateClassSubjectCourse(Long id, CreateClassSubjectRequest createClassRequest);

    ClassDto adminApproveRequestCreateClass(Long id) throws JsonProcessingException;

    ChangeInfoClassRequest adminRequestChangeInfoClass(Long id, String content);

    ApiPage<ClassDto> getClassRequesting(ClassSearchRequest query, Pageable pageable);

    Boolean studentEnrollClass(Long studentId, Long classId);

    List<ClassStudentDto> getStudentWaitingIntoClass(Long classId);

    ApiPage<ClassDto> searchClass(ClassSearchRequest query, Pageable pageable);


    ClassDto classDetail(Long id) throws JsonProcessingException;

    ApiPage<ClassDto> getAllClass(Pageable pageable);

    ApiPage<ClassDto> getAllClassForUser(Pageable pageable, GuestSearchClassRequest guestSearchClassRequest);

    ApiPage<ClassDto> accountFilterClass(ClassSearchRequest query, Pageable pageable);

    ApiPage<ClassDto> classSuggestion(long infoFindTutorId, Pageable pageable);

    Long createClassForRecruiting(CreateRecruitingClassRequest createRecruitingClassRequest) throws JsonProcessingException, ParseException;

    Boolean applyToRecruitingClass(Long classId);

    AccountResponse chooseCandicateForClass(ClassCandicateRequest request);

    ApiPage<CandicateResponse> getClassCandicate(Long classId, Pageable pageable);

    ApiPage<ClassDto> getRecruitingClasses(Pageable pageable);

    ApiPage<ClassDto> getClassByAccount(ClassSearchRequest request, Pageable pageable);

    ClassDetailDto accountGetClassDetail(Long id);

    ClassResourcesResponse accountGetResourceOfClass(Long id);

    ApiPage<AccountResponse> accountGetStudentOfClass(Long id, Pageable pageable);

    List<ClassTimeTableResponse> accountGetTimeTableOfClass(Long id);

    ClassTeacherResponse studentGetTeacherInfoOfClass(Long id);


    ClassAttendanceResponse accountGetAttendanceOfClass(Long id);

    ApiPage<ClassDto> adminGetClass(EClassStatus status, Pageable pageable);

    List<ClassDto> getClassByAccountAsList(EClassStatus status);

    ClassDto confirmAppreciation(Long id) throws JsonProcessingException;

    Long updateClassForRecruiting(Long id, CreateRecruitingClassRequest createRecruitingClassRequest) throws ParseException;

    ClassDto adminRejectRequestCreateClass(Long id) throws JsonProcessingException;

    Boolean adminEnrolStudentIntoClass(Long studentId, Long classId) throws JsonProcessingException;

    ClassDto cancelPendingClass(Long classId);

    Boolean detectExpireRecruitingClass();

    Boolean confirmTeaching(String confirmCode) throws JsonProcessingException;

    Boolean detectExpireConfirmation();


    Long updateClassForRequesting(Long id, CreateRequestingClassRequest createRequestingClassRequest) throws ParseException;
}
