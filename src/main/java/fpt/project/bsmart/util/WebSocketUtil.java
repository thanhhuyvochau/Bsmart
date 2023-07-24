package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.dto.notification.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WebSocketUtil {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketUtil(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public static final String BASE_TOPIC = "/topic/message";
    public static final String QUEUE_PRIVATE = "/queue/private-message";

    public void sendNotification(final String topic, final String message) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSend(BASE_TOPIC, res);
    }

    public void sendPrivateNotification(final String message) {
        ResponseMessage res = new ResponseMessage(message);
        Optional<String> usernameOptional = SecurityUtil.getCurrentUserName();
        usernameOptional.ifPresent(s -> messagingTemplate.convertAndSendToUser(s, QUEUE_PRIVATE, res));
    }
}
