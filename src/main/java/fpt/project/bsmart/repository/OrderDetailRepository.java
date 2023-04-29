package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.OrderDetail;
import fpt.project.bsmart.entity.ReferralCode;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {


}
