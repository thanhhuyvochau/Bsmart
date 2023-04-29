package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Order;
import fpt.project.bsmart.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


}
