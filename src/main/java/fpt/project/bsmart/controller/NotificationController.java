package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.request.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send-notification")
    public String sendMessage(@RequestBody NotificationMessage notification) {
        simpMessagingTemplate.convertAndSend("/topic/" + notification.getTopic(), notification.getMessage());
        return "hay";
    }
}


