package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByWallet(Wallet wallet, Pageable pageable);
}
