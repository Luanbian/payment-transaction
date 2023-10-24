package picpay.transaction.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.domain.users.User;
import picpay.transaction.infra.repositories.TransactionRepository;

public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserService userService;

    public void create(TransactionDto transactionDto) throws Exception {
        User sender = this.userService.findById(transactionDto.senderId());
        User receiver = this.userService.findById(transactionDto.receiverId());

        userService.validateTransaction(sender, transactionDto.value());
    }
}
