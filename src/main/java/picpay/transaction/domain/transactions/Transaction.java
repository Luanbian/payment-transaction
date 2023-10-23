package picpay.transaction.domain.transactions;

import jakarta.persistence.*;
import lombok.Data;
import picpay.transaction.domain.users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Data
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User sender;

    private LocalDateTime timestamp;
}
