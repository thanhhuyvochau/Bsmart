package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.Notifier;
import fpt.project.bsmart.entity.User;

import java.util.Objects;
import java.util.Optional;

public class NotificationUtil {
    public static Notifier findNotifier(Notification notification, User user) {
        Optional<Notifier> optionalNotifier = notification.getNotifiers().stream().filter(notifier -> Objects.equals(notifier.getUser().getId(), user.getId())).findFirst();
        return optionalNotifier.orElse(null);
    }
}
