package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(final String message) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSend("/receive/message", res);
    }

    @Override
    public void sendPrivateMessage(String message, String id) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSendToUser(id, "/receive/private-message", res);
    }

    @Override
    public void sendNotification(String message) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSend("/receive/global-notification", res);
    }

    @Override
    public void sendPrivateNotification(String message, String id) {
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSendToUser(id, "/receive/private-notification", res);
    }
}
