package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.dto.ResponseMessage;
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

        notificationService.sendGlobalNotification();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setContent("test");
        messagingTemplate.convertAndSend("/topic/messages", responseMessage);

    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage ResponseMessage = new ResponseMessage(message);

        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", ResponseMessage);
    }

    public String sayHello() {
        notificationService.sendSayHello();
        messagingTemplate.convertAndSend("/", "Just say hello");
        return "Just say hello";
    }
}
