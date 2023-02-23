package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.moodle.response.MoodleAllClassRecourseDtoResponse;
import fpt.project.bsmart.moodle.response.MoodleRecourseClassesDtoResponse;
import fpt.project.bsmart.moodle.response.MoodleRecourseDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IExerciseService {


    List<MoodleRecourseDtoResponse> getExerciseInClass(Long classId);

    List<MoodleRecourseDtoResponse> teacherGetExerciseInClass(Long classId);

    ApiPage<MoodleRecourseClassesDtoResponse> studentGetAllExerciseAllClass(Pageable pageable);

    ApiPage<MoodleAllClassRecourseDtoResponse> teacherGetAllExerciseAllClass(Pageable pageable);

    List<Long> teacherGetAllSubmitStudent( Long instanceId ,Long classId  ) throws JsonProcessingException;
}
