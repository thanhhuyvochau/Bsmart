package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.FeedBack;
import fpt.project.bsmart.entity.StudentClassKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {

    FeedBack findFeedBackByStudentClassKeyIdAndTeacherId (StudentClassKey studentClassKey , Long teacherId ) ;


}
