package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;

@Component
@Transactional
public class WebsocketPublisher {
    private static SimpMessagingTemplate staticSimpMessagingTemplate;


    private static NotificationRepository staticNotificationRepository;

    public WebsocketPublisher(NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate) {
        staticSimpMessagingTemplate = simpMessagingTemplate;
        staticNotificationRepository = notificationRepository;
    }

    public static void publishNotification(Notification notification) {
        if (notification == null) {
            return;
        }
        User user = notification.getUser();

        if (user == null) {
            return;
        }
        String email = user.getEmail();
        if (email == null) {
            return;
        }
        Long count = staticNotificationRepository.countAllByUser(user);

        String destination = "/topic/notification/" + email;

        staticSimpMessagingTemplate.convertAndSend(destination);
    }


}
