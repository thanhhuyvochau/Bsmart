package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByQuestion_IdAndParentCommentIsNull(Long questionId);
}
