package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.OrderDetail;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    List<OrderDetail> findAllBySubCourse(SubCourse subCourse);
    List<OrderDetail> findAllByClazz(Class clazz);
    @Query("SELECT od FROM OrderDetail od WHERE od.clazz = :clazz AND od.order.status = :status")
    List<OrderDetail> findAllByClazzAndStatus(Class clazz, EOrderStatus status);

}
