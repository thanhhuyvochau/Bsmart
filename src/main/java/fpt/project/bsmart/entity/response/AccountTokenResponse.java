package fpt.project.bsmart.entity.response;

public class AccountTokenResponse {
    private String token;
    private String message;

    public AccountTokenResponse(String token, String s) {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
