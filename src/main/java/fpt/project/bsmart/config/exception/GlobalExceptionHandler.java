package fpt.project.bsmart.config.exception;

import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static String defaultErrorMessage = "Đã có lỗi ngoại lệ của hệ thống xảy ra, vui lòng liên hệ admin để được hỗ trợ";

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> handleUnAuthorizedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failed("Access Denied"));
    }

    @ExceptionHandler({ApiException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.failed(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> handleCommonException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failed(e.getMessage() == null ? defaultErrorMessage : e.getMessage()));
    }


}