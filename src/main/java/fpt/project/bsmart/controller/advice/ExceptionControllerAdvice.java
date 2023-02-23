package fpt.project.bsmart.controller.advice;

import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> handleUnAuthorizedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failed("Access Denied"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleCommonException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failed(e.getMessage()));
    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleCommonException(ApiException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.failed(e.getMessage()));
    }
}