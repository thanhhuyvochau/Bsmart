package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExerciseRepository extends JpaRepository<FileAttachment, Long> {


}
