package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Notifier;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.repository.NotifierRepository;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class NotificationService {
    private final NotifierRepository notifierRepository;

    public NotificationService(NotifierRepository notifierRepository) {
        this.notifierRepository = notifierRepository;
    }

    public ApiPage<ResponseMessage> getNotifications(Pageable pageable) {
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Page<Notifier> notifiers = notifierRepository.findAllByUser(currentUser, pageable);
        return PageUtil.convert(notifiers.map(notifier -> ConvertUtil.convertNotificationToResponseMessage(notifier.getNotification(), notifier.getUser())));
    }
}
