package fpt.project.bsmart.moodle.config;

import org.springframework.http.HttpHeaders;

public class HttpHeadersCustom extends HttpHeaders {
    @Override
    public void setBearerAuth(String token) {
        this.set("Authorization",token);
    }
}
