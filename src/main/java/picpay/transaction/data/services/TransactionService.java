package picpay.transaction.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.domain.transactions.Transaction;
import picpay.transaction.domain.users.User;
import picpay.transaction.infra.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private NotificationService notificationService;

    public Transaction create(TransactionDto transactionDto) throws Exception {
        User sender = this.userService.findById(transactionDto.senderId());
        User receiver = this.userService.findById(transactionDto.receiverId());

        userService.validateTransaction(sender, transactionDto.value());

        boolean isAuthorized = this.authorizationService.authorizeTransaction(sender, transactionDto.value());
        if(!isAuthorized) throw new Exception("Transação não autorizada");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDto.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDto.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDto.value()));

        this.repository.save(newTransaction);
        this.userService.save(sender);
        this.userService.save(receiver);

        this.notificationService.send(sender, "Transação realizada com sucesso");
        this.notificationService.send(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }

    public List<Transaction> getAll() {
        return this.repository.findAll();
    }
}
