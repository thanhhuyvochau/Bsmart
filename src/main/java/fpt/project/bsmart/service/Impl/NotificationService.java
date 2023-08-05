package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.Notifier;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.repository.NotificationRepository;
import fpt.project.bsmart.repository.NotifierRepository;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.NotificationUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {
    private final NotifierRepository notifierRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(NotifierRepository notifierRepository, NotificationRepository notificationRepository) {
        this.notifierRepository = notifierRepository;
        this.notificationRepository = notificationRepository;
    }

    public ApiPage<ResponseMessage> getNotifications(Pageable pageable) {
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Page<Notifier> notifiers = notifierRepository.findAllByUser(currentUser, pageable);
        return PageUtil.convert(notifiers.map(notifier -> ConvertUtil.convertNotificationToResponseMessage(notifier.getNotification(), notifier.getUser())));
    }

    public Boolean readNotification(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        Notification notification = optionalNotification.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Thông báo không tìm thấy với id:" + id));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Notifier notifier = NotificationUtil.findNotifier(notification, currentUser);
        if (notifier != null) {
            if (notifier.isRead()) {
                return true;
            }
            notifier.setRead(true);
            return true;
        }
        return false;
    }
}
