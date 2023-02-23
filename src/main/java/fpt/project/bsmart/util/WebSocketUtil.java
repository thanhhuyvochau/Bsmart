package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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

    public void sendPrivateNotification(final String message, final String id) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSendToUser(id, QUEUE_PRIVATE, res);
    }
}
