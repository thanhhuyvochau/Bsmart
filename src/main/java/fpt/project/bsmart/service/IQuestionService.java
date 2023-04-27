package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.QuestionDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IQuestionService {
    Boolean importQuestion(MultipartFile file, Long subjectId) throws IOException;
}
