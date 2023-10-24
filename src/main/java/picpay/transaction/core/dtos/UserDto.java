package picpay.transaction.core.dtos;

import picpay.transaction.domain.users.UserType;

import java.math.BigDecimal;

public record UserDto(
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        String email,
        String password,
        UserType userType
) {}
