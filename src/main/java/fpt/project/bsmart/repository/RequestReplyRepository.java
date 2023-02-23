package fpt.project.bsmart.repository;


import fpt.project.bsmart.entity.RequestReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface RequestReplyRepository extends JpaRepository<RequestReply, Long> {



}
