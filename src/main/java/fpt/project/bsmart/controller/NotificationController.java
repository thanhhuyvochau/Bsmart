package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.request.NotificationMessage;
import fpt.project.bsmart.entity.response.Message;
import fpt.project.bsmart.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import fpt.project.bsmart.entity.dto.ResponseMessage;

import java.security.Principal;

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


