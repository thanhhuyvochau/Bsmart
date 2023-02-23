package fpt.project.bsmart.entity.common;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private HttpStatus status;
    private String message;

    private String internalMessage;

    private ApiException() {
    }

    public static ApiException create(HttpStatus status) {
        ApiException exception = new ApiException();
        exception.status = status;
        return exception;
    }

    public ApiException withMessage(String message) {
        this.message = message;

        return this;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
