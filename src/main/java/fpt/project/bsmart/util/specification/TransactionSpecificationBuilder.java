package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TransactionSpecificationBuilder {
    public static TransactionSpecificationBuilder transactionSpecificationBuilder(){
        return new TransactionSpecificationBuilder();
    }

    private List<Specification<Transaction>> specifications = new ArrayList<>();

    public TransactionSpecificationBuilder filterFromDate(Instant date){
        if(date == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Transaction_.CREATED), date));
        return this;
    }

    public TransactionSpecificationBuilder filterToDate(Instant date){
        if(date == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Transaction_.CREATED), date));
        return this;
    }

    public TransactionSpecificationBuilder filterByBuyer(Long buyer){
        if(buyer == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Order> transactionOrderJoin = root.join(Transaction_.order);
            Join<Order, User> orderUserJoin = transactionOrderJoin.join(Order_.USER);
            return criteriaBuilder.equal(orderUserJoin.get(User_.ID), buyer);
        });
        return this;
    }

    public TransactionSpecificationBuilder filterBySeller(Long seller){
        if(seller == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Order> transactionOrderJoin = root.join(Transaction_.order);
            Join<Order, OrderDetail> orderOrderDetailJoin = transactionOrderJoin.join(Order_.ORDER_DETAILS);
            Join<OrderDetail, Class> orderDetailClassJoin = orderOrderDetailJoin.join(OrderDetail_.CLAZZ);
            Join<Class, User> classUserJoin = orderDetailClassJoin.join(Class_.MENTOR);
            return criteriaBuilder.equal(classUserJoin.get(User_.ID), seller);
        });
        return this;
    }

    public TransactionSpecificationBuilder filterByCourse(Long courseId){
        if(courseId == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            Join<Transaction, Order> transactionOrderJoin = root.join(Transaction_.order);
            Join<Order, OrderDetail> orderOrderDetailJoin = transactionOrderJoin.join(Order_.ORDER_DETAILS);
            Join<OrderDetail, Class> orderDetailClassJoin = orderOrderDetailJoin.join(OrderDetail_.CLAZZ);
            Join<Class, Course> classCourseJoin = orderDetailClassJoin.join(Class_.COURSE);
            return criteriaBuilder.equal(classCourseJoin.get(Course_.ID), courseId);
        });
        return this;
    }

    public TransactionSpecificationBuilder filterByStatus(ETransactionStatus status){
        if(status == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Transaction_.STATUS), status));
        return this;
    }

    public TransactionSpecificationBuilder filterByTpe(ETransactionType type){
        if(type == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Transaction_.TYPE), type));
        return this;
    }

    public Specification<Transaction> all(){
        return Specification.where(null);
    }

    public Specification<Transaction> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }
}
