package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.response.Message;
import fpt.project.bsmart.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/websocket")
public class WSController {

  private final NotificationService notificationService ;

  public WSController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping("/send-message")
  public String sendMessage(@RequestBody Message message){
    String s = notificationService.sendMessage(message.getMessageContent());
    return s ;
  }

  @PostMapping("/send-private-message/{id}")
  public void sendPrivateMessage(@RequestBody Message message, @PathVariable String id){
    notificationService.sendPrivateMessage(message.getMessageContent(),id);
  }

  @RequestMapping(value = "/send-notification-global",method = RequestMethod.POST)
  public void sendNotification(@RequestBody Message message){
    System.out.println("hello");
    notificationService.sendNotification(message.getMessageContent());
  }

  @RequestMapping(value = "/send-notification-private/{id}",method = RequestMethod.POST)
  public void sendPrivateNotification(@RequestBody Message message, @PathVariable String id){
    notificationService.sendPrivateNotification(message.getMessageContent(),id);
  }
}