package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoteUtil {
    private final SecurityUtil securityUtil;

    public VoteUtil(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    public static VoteNumberReponse getVoteResponse(Question question) {
        List<Vote> votes = question.getVotes();
        Integer upvote = 0;
        Integer downvote = 0;
        for (Vote vote : votes) {
            if (vote.getVote()) {
                upvote++;
            } else {
                downvote++;
            }
        }
        return new VoteNumberReponse(upvote, downvote);
    }

    public static VoteNumberReponse getVoteResponse(Comment comment) {
        List<Vote> votes = comment.getVotes();
        Integer upvote = 0;
        Integer downvote = 0;
        for (Vote vote : votes) {
            if (vote.getVote()) {
                upvote++;
            } else {
                downvote++;
            }
        }
        return new VoteNumberReponse(upvote, downvote);
    }

    public static Integer getUserState(Question question, Account currentUser) {
        Vote vote = question.getVotes().stream()
                .filter(v -> v.getAccount().getId().equals(currentUser.getId())).findFirst().orElse(null);
        int actionState = 0;
        if (vote != null) {
            Boolean voteAction = vote.getVote();
            if (voteAction) {
                actionState = 1;
            } else {
                actionState = -1;
            }
        }
        return actionState;
    }

    public static Integer getUserState(Comment comment, Account currentUser) {
        Vote vote = comment.getVotes().stream()
                .filter(v -> v.getAccount().getId().equals(currentUser.getId())).findFirst().orElse(null);
        int actionState = 0;
        if (vote != null) {
            Boolean voteAction = vote.getVote();
            if (voteAction) {
                actionState = 1;
            } else {
                actionState = -1;
            }
        }
        return actionState;
    }
}
