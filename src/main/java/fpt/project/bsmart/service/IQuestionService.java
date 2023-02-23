package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.dto.QuestionSimpleDto;
import fpt.project.bsmart.entity.request.CreateQuestionRequest;
import fpt.project.bsmart.entity.request.VoteRequest;
import org.springframework.data.domain.Pageable;

public interface IQuestionService {
    QuestionDto getQuestion(Long id);
    QuestionDto createQuestion(CreateQuestionRequest createQuestionRequest);

    QuestionDto updateQuestion(Long questionId, CreateQuestionRequest createQuestionRequest);

    Boolean closeQuestion(Long questionId);

    Boolean openQuestion(Long id);

    Boolean voteQuestion(VoteRequest request);

    ApiPage<QuestionSimpleDto> searchQuestion(Long forumId, String q, Pageable pageable);
}
