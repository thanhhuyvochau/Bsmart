package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Comment;
import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Vote;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.CommentDto;
import fpt.project.bsmart.entity.request.CreateCommentRequest;
import fpt.project.bsmart.entity.request.VoteRequest;
import fpt.project.bsmart.repository.CommentRepository;
import fpt.project.bsmart.repository.QuestionRepository;
import fpt.project.bsmart.repository.VoteRepository;
import fpt.project.bsmart.service.ICommentService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final SecurityUtil securityUtil;
    private final QuestionRepository questionRepository;
    private final VoteRepository voteRepository;

    public CommentServiceImpl(CommentRepository commentRepository, SecurityUtil securityUtil, QuestionRepository questionRepository, VoteRepository voteRepository) {
        this.commentRepository = commentRepository;
        this.securityUtil = securityUtil;
        this.questionRepository = questionRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Comment not found by id:" + id));
        return ConvertUtil.doConvertEntityToResponse(comment, securityUtil.getCurrentUserThrowNotFoundException());
    }

    @Override
    public List<CommentDto> getCommentByQuestion(Long questionId) {
        Account currentAccount = securityUtil.getCurrentUserThrowNotFoundException();
        List<Comment> comments = commentRepository.findAllByQuestion_IdAndParentCommentIsNull(questionId);
        if (!comments.isEmpty()) {
            return comments.stream().map(comment -> ConvertUtil.doConvertEntityToResponse(comment, currentAccount))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public CommentDto createComment(CreateCommentRequest createCommentRequest) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Comment comment = ObjectUtil.copyProperties(createCommentRequest, new Comment(), Comment.class, true);
        Question question = questionRepository.findById(createCommentRequest.getQuestionId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Question not found with id:" + createCommentRequest.getQuestionId()));
        if (question.getClosed()) {
            throw ApiException.create(HttpStatus.CONFLICT)
                    .withMessage("Question has closed!!");
        }
        comment.setQuestion(question);
        comment.setAccount(account);
        if (createCommentRequest.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(createCommentRequest.getParentCommentId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Parent comment not found with id:" + createCommentRequest.getParentCommentId()));
            comment.setParentComment(parentComment);
        }
        comment = commentRepository.save(comment);
        return ConvertUtil.doConvertEntityToResponse(comment, account);
    }

    @Override
    public CommentDto updateComment(Long commentId, CreateCommentRequest createCommentRequest) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Comment not found with id:" + commentId));
        if (!Objects.equals(account.getId(), comment.getAccount().getId())) {
            throw ApiException.create(HttpStatus.CONFLICT)
                    .withMessage("Comment is edited by another user with username:" + account.getUsername());
        }
        comment.setContent(createCommentRequest.getContent());
        comment = commentRepository.save(comment);
        return ConvertUtil.doConvertEntityToResponse(comment, account);
    }

    @Override
    public Boolean deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Comment not found with id:" + commentId));
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public Boolean voteComment(VoteRequest request) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Comment not found with id:" + request.getCommentId()));
        Vote vote = voteRepository.findByCommentAndAccount(comment, account).orElse(new Vote());
        if (Objects.equals(request.getVote(), vote.getVote())) {
            comment.getVotes().remove(vote);
            voteRepository.delete(vote);
        } else {
            vote.setComment(comment);
            vote.setAccount(account);
            vote.setVote(request.getVote());
            voteRepository.save(vote);
        }
        return true;
    }
}
