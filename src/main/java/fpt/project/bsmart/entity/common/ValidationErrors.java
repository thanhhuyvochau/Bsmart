package fpt.project.bsmart.entity.common;

import java.util.ArrayList;

public class ValidationErrors <T>{
    private ValidationError<T> error;

    public static class ValidationError<T>{
        private String message;
        private ArrayList<T> invalidParams = new ArrayList<T>();

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<T> getInvalidParams() {
            return invalidParams;
        }

        public void setInvalidParams(ArrayList<T> invalidParams) {
            this.invalidParams = invalidParams;
        }
    }

    public ValidationError<T> getError() {
        return error;
    }

    public void setError(ValidationError<T> error) {
        this.error = error;
    }
}
