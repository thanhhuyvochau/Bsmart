package fpt.project.bsmart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.moodle.request.CreateCategoryRequest;
import fpt.project.bsmart.moodle.response.MoodleCategoryResponse;
import fpt.project.bsmart.moodle.response.MoodleCourseResponse;

import java.util.List;

public interface IMoodleService {


    List<MoodleCategoryResponse> getCategoryFromMoodle() throws JsonProcessingException;

    Boolean createCategoryToMoodle(CreateCategoryRequest.CreateCategoryBody createCategoryBody) throws JsonProcessingException;

    ApiPage<MoodleCourseResponse> synchronizedClassFromMoodle() throws JsonProcessingException;

    Boolean synchronizedAllClassDetailFromMoodle() throws JsonProcessingException;

    String enrolUserToCourseMoodle(Class clazz, Account account) throws JsonProcessingException;

    String unenrolUserToCourseMoodle(Class clazz) throws JsonProcessingException;

    Boolean synchronizedRoleFromMoodle() throws JsonProcessingException;
    Boolean synchronizedClassDetailFromMoodle(Class clazz) throws JsonProcessingException;
}
