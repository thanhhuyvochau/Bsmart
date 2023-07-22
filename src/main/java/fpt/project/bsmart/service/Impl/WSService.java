package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.dto.notification.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend() {
//        ResponseMessage response = new ResponseMessage("test send message");
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/messages", "test send message");

    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage response = new ResponseMessage(message);

        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", response);
    }

    public String sayHello() {
        notificationService.sendSayHello();
        messagingTemplate.convertAndSend("/topic/say-hello", "Just say hello");
        return "Just say hello";
    }
}
