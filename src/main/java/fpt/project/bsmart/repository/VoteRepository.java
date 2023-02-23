package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Comment;
import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByQuestion_Id(Long questionId);

    List<Vote> findAllByComment_Id(Long commentId);

    Optional<Vote> findByCommentAndAccount(Comment comment, Account account);

    Optional<Vote> findByQuestionAndAccount(Question question, Account account);
}
