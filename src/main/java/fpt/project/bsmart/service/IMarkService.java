package fpt.project.bsmart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.response.MarkResponse;

import java.util.List;


public interface IMarkService {
    List<MarkResponse> getStudentMark(Long clazzId,Long studentId);
    boolean synchronizeMark() throws JsonProcessingException;
}
