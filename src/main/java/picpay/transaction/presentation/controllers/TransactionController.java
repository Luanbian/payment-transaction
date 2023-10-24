package picpay.transaction.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.data.services.TransactionService;
import picpay.transaction.domain.transactions.Transaction;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> create (@RequestBody TransactionDto transactionDto) throws Exception {
        Transaction newTransaction = this.transactionService.create(transactionDto);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}
