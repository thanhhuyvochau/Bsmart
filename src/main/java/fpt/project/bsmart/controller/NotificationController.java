package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.response.Message;
import fpt.project.bsmart.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import fpt.project.bsmart.entity.dto.ResponseMessage;

import java.security.Principal;

@Controller
public class NotificationController {

    private final NotificationService notificationService ;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public NotificationController(NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/receive/message")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("recived message");
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }


    @MessageMapping("/private-message")
    @SendToUser("/receive/private-message")
    public ResponseMessage getPrivateMessage(final Message mess, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("recived message private"+principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape("Sending personal message to user"+principal.getName()+": "+ mess.getMessageContent()));
    }

    //client has to send the message to the destination /app/application to reach this handler.
    @MessageMapping("/application") // accepting messages on the /application endpoint.
    @SendTo("/all/messages") //we forward the incoming message to /all/messages
    // Now all clients subscribing to the messages on this destination will get the messages
    // sent to all the clients.
    public Message send(final Message message) throws Exception {
        return message;
    }

}


