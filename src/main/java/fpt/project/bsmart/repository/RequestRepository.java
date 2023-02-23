package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {


    Page<Request> findAll(Specification<Request> spec, Pageable pageable);

}
