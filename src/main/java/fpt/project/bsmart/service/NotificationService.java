package fpt.project.bsmart.service;

public interface NotificationService {
    public String  sendMessage(final String message) ;

    public void sendPrivateMessage(final String message, final String id) ;

    public void sendNotification(final String message);

    public void sendPrivateNotification(final String message,final String id) ;
}
