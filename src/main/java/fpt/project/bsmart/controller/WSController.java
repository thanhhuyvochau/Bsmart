package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.dto.notification.Message;
import fpt.project.bsmart.service.Impl.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSController {

    @Autowired
    private WSService service;

    @PostMapping("/send-message")
    public void sendMessage() {
        service.notifyFrontend();
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final Message message) {
        service.notifyUser(id, message.getMessageContent());
    }

    @PostMapping("/say-hello")
    public String sayHello() {
        String s = service.sayHello();
        System.out.println(s);
        return s ;
    }
}
