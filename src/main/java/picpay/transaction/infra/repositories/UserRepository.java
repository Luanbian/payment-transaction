package picpay.transaction.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import picpay.transaction.domain.users.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByDocument(String document);
    Optional<User> findUserById(Long id);
}
