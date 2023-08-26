package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.Notifier;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebSocketUtil {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketUtil(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public static final String BASE_TOPIC = "/topic/message";
    public static final String QUEUE_PRIVATE = "/queue/message";

    public void sendNotification(String topic, ResponseMessage message) {
        if (topic == null || topic.isEmpty()) {
            topic = BASE_TOPIC;
        }
        ApiResponse<ResponseMessage> apiResponse = ApiResponse.success(message);
        messagingTemplate.convertAndSend(topic, apiResponse);
    }

    public void sendPrivateNotification(Notification notification) {
        List<User> receivedUsers = notification.getNotifiers().stream().map(Notifier::getUser).collect(Collectors.toList());
        for (User receivedUser : receivedUsers) {
            ResponseMessage responseMessage = ConvertUtil.convertNotificationToResponseMessage(notification, receivedUser);
            sendPrivateNotification(receivedUser.getEmail(), responseMessage);
        }
    }

    public void sendPrivateNotification(String userID, ResponseMessage message) {
        ApiResponse<ResponseMessage> apiResponse = ApiResponse.success(message);
        System.out.println("------------Subcribe URL:" + QUEUE_PRIVATE + userID);
        messagingTemplate.convertAndSend(QUEUE_PRIVATE + userID, apiResponse);
    }
}
