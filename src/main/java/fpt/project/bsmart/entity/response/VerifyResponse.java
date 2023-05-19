package fpt.project.bsmart.entity.response;

import org.springframework.http.HttpStatus;

public class VerifyResponse {
    private String message;
    private String status;


    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public static class Builder {
        private String message;
        private String status;
        private VerifyResponse verifyResponse;

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder build() {
            this.verifyResponse = new VerifyResponse();
            this.verifyResponse.setMessage(message);
            this.verifyResponse.setStatus(status);
            return this;
        }

        public VerifyResponse getObject() {
            return verifyResponse;
        }
    }
}
