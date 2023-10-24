package picpay.transaction.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picpay.transaction.domain.transactions.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
