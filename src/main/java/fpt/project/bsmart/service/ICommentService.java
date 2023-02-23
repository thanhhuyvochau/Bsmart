package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.CommentDto;
import fpt.project.bsmart.entity.request.CreateCommentRequest;
import fpt.project.bsmart.entity.request.VoteRequest;

import java.util.List;

public interface ICommentService {
    CommentDto getComment(Long id);

    List<CommentDto> getCommentByQuestion(Long questionId);

    CommentDto createComment(CreateCommentRequest createCommentRequest);

    CommentDto updateComment(Long commentId, CreateCommentRequest createCommentRequest);

    Boolean deleteComment(Long commentId);
    Boolean voteComment(VoteRequest request);
}
