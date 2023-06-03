package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class NotificationUtil {


    private final NotificationRepository notificationRepository;


    @Autowired
    public NotificationUtil(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(String title, String content, Map<String, Object> data, User user) {
        if (user != null) {
            sendNotification(title, content, data,user);
        }

    }

    public void sendNotification(String title, String content, String data,  User user) {
        try {
            Notification notification = new Notification () ;
            notification.setViTitle(title) ;
            notification.setData(data) ;
            notification.setViContent(content);
            notification.setUser(user);

            sendNotification(notification);
        } catch (Exception e) {

        }
    }

    public void sendNotification(Notification notification) {
        Notification persistedNotification = notificationRepository.save(notification);
        User user = persistedNotification.getUser() ;

        if (user != null) {
            sendNotification(notification, user);
        }
    }


    public void sendNotification(Notification notification,  User user) {
        // send by websocket
        WebsocketPublisher.publishNotification(notification);
    }
}
