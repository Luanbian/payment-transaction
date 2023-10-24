package picpay.transaction.core.dtos;

public record ExceptionDto(
        String message,
        String statusCode
) {}
