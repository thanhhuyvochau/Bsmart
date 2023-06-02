package fpt.project.bsmart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;


@Controller
public class NotificationController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/websocket")
    public void send(SimpMessageHeaderAccessor sha, @Payload String username) {
        String message = "Hello from " + sha.getUser().getName();

        simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }
}


