package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByPaymentClass(Class aClass);

    List<Transaction> findAllByPaymentClassIn(List<Class> aClass);

    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);

    List<Transaction> findAll(Specification<Transaction> spec);

    List<Transaction> findByPaymentClassAndAccount(Class clazz, Account student);
}
