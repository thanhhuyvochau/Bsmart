package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.StudentClass;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    Optional<StudentClass> findByClazzAndStudent (Class clazz, User student) ;
}
