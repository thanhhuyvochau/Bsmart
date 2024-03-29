package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.Wallet;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByWallet(Wallet wallet, Pageable pageable);
    Page<Transaction> findAllByType(ETransactionType type, Pageable pageable);
    Page<Transaction> findAll(Specification<Transaction> specification, Pageable pageable);
    List<Transaction> findAll(Specification<Transaction> specification);
    List<Transaction> findAllByStatusAndType(ETransactionStatus status, ETransactionType type);
}
