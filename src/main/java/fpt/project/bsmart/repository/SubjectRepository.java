package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT e from Subject e WHERE e.id in :idList")
    public List<Subject> getSubjectsByIdList(@Param("idList") List<Long> idList);
}
