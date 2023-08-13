package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecificationBuilder {
    public static TransactionSpecificationBuilder transactionSpecificationBuilder(){return new TransactionSpecificationBuilder();}
    private List<Specification<Transaction>> specifications = new ArrayList<>();

    public TransactionSpecificationBuilder filterByStatus(ECourseStatus status){
        if(status == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Transaction_.STATUS), status));
        return this;
    }

    public TransactionSpecificationBuilder filterByType(ETransactionType type){
        if(type == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Transaction_.TYPE), type));
        return this;
    }

}
