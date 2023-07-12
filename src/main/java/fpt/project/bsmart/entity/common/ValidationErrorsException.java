package fpt.project.bsmart.entity.common;

import java.util.List;

public class ValidationErrorsException extends Exception  {
    private List<?> invalidFields;
    private String errorMessage;


    public List<?> getInvalidFields() {
        return invalidFields;
    }

    public void setInvalidFields(List<String> invalidFields) {
        this.invalidFields = invalidFields;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ValidationErrorsException(List<?> invalidFields, String errorMessage) {
        this.invalidFields = invalidFields;
        this.errorMessage = errorMessage;
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("ValidationErrorsException: ").append(errorMessage).append("\n");
//        sb.append("Invalid Fields: ").append(String.join(",", invalidFields));
//        return sb.toString();
//    }
}
