package picpay.transaction.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picpay.transaction.core.dtos.UserDto;
import picpay.transaction.domain.users.User;
import picpay.transaction.domain.users.UserType;
import picpay.transaction.infra.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação");
        }
        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User create (UserDto data) {
        User newUser = new User(data);
        this.save(newUser);
        return newUser;
    }

    public List<User> getAll () {
        return this.repository.findAll();
    }

    public void save(User user) {
        this.repository.save(user);
    }
}
