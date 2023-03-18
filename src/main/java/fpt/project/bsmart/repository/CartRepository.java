package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Cart;
import fpt.project.bsmart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
