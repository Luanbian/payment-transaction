package picpay.transaction.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.data.services.TransactionService;
import picpay.transaction.domain.transactions.Transaction;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Transaction>> list () {
        List<Transaction> transactions = this.transactionService.getAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
