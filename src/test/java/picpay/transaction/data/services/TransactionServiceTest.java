package picpay.transaction.data.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import picpay.transaction.core.dtos.TransactionDto;
import picpay.transaction.core.dtos.UserDto;
import picpay.transaction.domain.users.User;
import picpay.transaction.domain.users.UserType;
import picpay.transaction.infra.repositories.TransactionRepository;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private NotificationService notificationService;
    @Autowired
    @InjectMocks
    private TransactionService transactionService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction when everything is Ok")
    void createSuccess() throws Exception{
        User sender = this.createSender();
        User receiver = this.createReceiver();

        when(userService.findById(1L)).thenReturn(sender);
        when(userService.findById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDto transactionDto = new TransactionDto(new BigDecimal(10), 1L, 2L);
        transactionService.create(transactionDto);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).save(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).save(receiver);

        verify(notificationService, times(1)).send(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).send(receiver, "Transação recebida com sucesso");
    }

    @Test
    @DisplayName("Should throw exception when transaction is not allowed")
    void createException() throws Exception{
        User sender = this.createSender();
        User receiver = this.createReceiver();

        when(userService.findById(1L)).thenReturn(sender);
        when(userService.findById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            TransactionDto transactionDto = new TransactionDto(new BigDecimal(10), 1L, 2L);
            transactionService.create(transactionDto);
        });

        Assertions.assertEquals("Transação não autorizada", exception.getMessage());
    }

    private User createSender() {
        UserDto userSenderDto = new UserDto(
                "Luan",
                "Almeida",
                "501934",
                new BigDecimal(10),
                "luan.almeida@gmail.com",
                "senha",
                UserType.COMMOM
        );
        User sender = new User(userSenderDto);
        return sender;
    }

    private User createReceiver() {
        UserDto userReceiverDto = new UserDto(
                "Maria",
                "Julia",
                "03838",
                new BigDecimal(10),
                "maria.julia@gmail.com",
                "senha",
                UserType.COMMOM
        );
        User receiver = new User(userReceiverDto);
        return receiver;
    }
}