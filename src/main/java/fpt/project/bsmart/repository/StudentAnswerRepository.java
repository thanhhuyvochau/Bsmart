package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Long> {



}
