package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    List<OrderDetail> findAllBySubCourse(SubCourse subCourse);
}
