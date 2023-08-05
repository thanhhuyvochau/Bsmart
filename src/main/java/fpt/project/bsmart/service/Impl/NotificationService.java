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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Boolean readNotification(Long[] ids) {
        List<Notification> notifications = notificationRepository.findAllById(Arrays.asList(ids));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (notifications.size() != ids.length) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Thông báo cần tìm không tìm thấy vui lòng thử lại!");
        }
        List<Notifier> updatedNotifier = new ArrayList<>();
        for (Notification notification : notifications) {
            Notifier notifier = NotificationUtil.findNotifier(notification, currentUser);
            if (notifier != null) {
                if (notifier.isRead()) {
                    continue;
                }
                updatedNotifier.add(notifier);
                notifier.setRead(true);
            }
        }
        notifierRepository.saveAll(updatedNotifier);
        return true;
    }
}
