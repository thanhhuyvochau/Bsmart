package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.EClassStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {

    Class findByCode(String code);

    Class findByCourseAndAccount(Course course, Account account);

    Class findByIdAndAccount(Long id, Account account);

    List<Class> findByAccountAndStatus(Account account, EClassStatus status);

    Page<Class> findAllByAccount(Account account, Pageable pageable);

    List<Class> findAllByAccount(Account account);


    Page<Class> findAllByAccountAndStatus(Account account, EClassStatus status, Pageable pageable);

    List<Class> findAllByAccountAndStatus(Account account, EClassStatus status);

    Page<Class> findAllByAccountAndActiveIsTrue(Account account, Pageable pageable);


    Optional<Class> findByIdAndCourse(long course, Course account);

    Page<Class> findAllByIsActiveIsTrue(Pageable pageable);


    List<Class> findAll(Specification<Class> spec);

    Page<Class> findAll(Specification<Class> spec, Pageable pageable);

    boolean existsByCode(String code);

    List<Class> findByIdInAndStatus(List<Long> classId, EClassStatus status);

    Page<Class> findAllByStatus(EClassStatus eClassStatus, Pageable pageable);

    List<Class> findAllByStatus(EClassStatus eClassStatus);

    Class findByMoodleClassId(Long id);
}
