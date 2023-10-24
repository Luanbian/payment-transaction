package picpay.transaction.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import picpay.transaction.core.dtos.NotificationDto;
import picpay.transaction.domain.users.User;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void send(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDto notificationRequest = new NotificationDto(email, message);
        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("url", notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            System.out.println("Erro ao enviar notificação");
            throw new Exception("Serviço de notificação fora do ar");
        }
    }
}