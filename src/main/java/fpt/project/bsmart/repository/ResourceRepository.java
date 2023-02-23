package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.AccountDetail;
import fpt.project.bsmart.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findAllByAccountDetail(AccountDetail accountDetail);

}
