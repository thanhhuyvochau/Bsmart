package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.service.Impl.NotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<ApiPage<ResponseMessage>>> getNotifications(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getNotifications(pageable)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<Boolean>> readNotification(Long[] ids) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.readNotification(ids)));
    }

}


