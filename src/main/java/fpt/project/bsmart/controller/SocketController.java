package fpt.project.bsmart.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting() {
        return "Hello you guy";
    }
}
