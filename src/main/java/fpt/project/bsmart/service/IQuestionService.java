package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.request.AddQuestionRequest;
import fpt.project.bsmart.entity.request.EditQuestionRequest;
import fpt.project.bsmart.entity.request.QuestionFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IQuestionService {
    Boolean importQuestionToQuestionBank(MultipartFile file, Long subjectId) throws IOException; // Only for Multiple Choice Question

    Boolean addQuestionToQuestionBank(AddQuestionRequest request);

    Boolean editQuestionToQuestionBank(Long id, EditQuestionRequest request);

    ApiPage<QuestionDto> getQuestions(QuestionFilter filter, Pageable pageable);

    QuestionDto getQuestion(Long id);


}
