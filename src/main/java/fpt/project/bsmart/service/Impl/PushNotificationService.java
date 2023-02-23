package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.request.PushNotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {





    private FCMService fcmService;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {

        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {

        }
    }
}