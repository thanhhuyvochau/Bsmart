package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.OrderDetail;
import fpt.project.bsmart.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
