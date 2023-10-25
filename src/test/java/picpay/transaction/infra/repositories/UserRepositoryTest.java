package picpay.transaction.infra.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import picpay.transaction.core.dtos.UserDto;
import picpay.transaction.domain.users.User;
import picpay.transaction.domain.users.UserType;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Test
    @DisplayName("Should get user successfully from database")
    void findUserByDocumentSuccess() {
        String document = "50193403838";
        UserDto data = new UserDto(
                "Luan",
                "Almeida",
                document,
                new BigDecimal(10),
                "luan.almeida@gmail.com",
                "senha",
                UserType.COMMOM
        );
        this.create(data);
        Optional<User> result = this.userRepository.findUserByDocument(document);
        assertThat(result.isPresent()).isTrue();
    }

    private User create(UserDto data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}