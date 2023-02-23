package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Transaction;
import fpt.project.bsmart.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionUtil {
    private final TransactionRepository transactionRepository;

    public TransactionUtil(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean isClassPaid(Account student, Class clazz) {
        List<Transaction> transactions = transactionRepository.findByPaymentClassAndAccount(clazz, student);
        long count = transactions.stream()
                .filter(transaction -> transaction.getSuccess() != null)
                .filter(Transaction::getSuccess).count();
        if (count==1) return true;
        return false;
    }
}
