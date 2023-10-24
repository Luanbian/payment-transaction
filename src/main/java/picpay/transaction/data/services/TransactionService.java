package picpay.transaction.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.domain.transactions.Transaction;
import picpay.transaction.domain.users.User;
import picpay.transaction.infra.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    public void create(TransactionDto transactionDto) throws Exception {
        User sender = this.userService.findById(transactionDto.senderId());
        User receiver = this.userService.findById(transactionDto.receiverId());

        userService.validateTransaction(sender, transactionDto.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transactionDto.value());
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
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
       ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("url", Map.class);
       if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
           String message = (String) authorizationResponse.getBody().get("message");
           return "Autorizado".equalsIgnoreCase(message);
       } else {
           return false;
       }
    }
}