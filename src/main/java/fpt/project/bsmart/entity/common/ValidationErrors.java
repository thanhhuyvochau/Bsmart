package fpt.project.bsmart.entity.common;

import java.util.ArrayList;

public class ValidationErrors {
    private ValidationError error  ;

    public static class ValidationError{
        private String message;
        private ArrayList<String> invalidParams = new ArrayList<String>();

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<String> getInvalidParams() {
            return invalidParams;
        }

        public void setInvalidParams(ArrayList<String> invalidParams) {
            this.invalidParams = invalidParams;
        }
    }

    public ValidationError getError() {
        return error;
    }

    public void setError(ValidationError error) {
        this.error = error;
    }



}
