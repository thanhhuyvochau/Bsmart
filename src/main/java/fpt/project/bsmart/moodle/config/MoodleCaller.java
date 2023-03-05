package fpt.project.bsmart.moodle.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.util.MapperUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Do Moodle không phải là RestFul nên chỉ cần 2 method để call qua đó
 */
@Component
public class MoodleCaller {


    private final RestTemplate restTemplate;

    @Value("${moodle.token}")
    private String token;

    public MoodleCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public <RequestType, ResponseType> ResponseType get(String uri, RequestType request, TypeReference<ResponseType> typeReference) throws JsonProcessingException {
        HttpHeadersCustom headers = new HttpHeadersCustom();
        //Set Content Type
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String requestAsString = MapperUtil.getInstance().getMapper().writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestAsString, headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        try {
            return MapperUtil.getInstance().getMapper().readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <RequestType, ResponseType> ResponseType post(String uri, RequestType request, TypeReference<ResponseType> typeReference) throws JsonProcessingException {
        HttpHeadersCustom headers = new HttpHeadersCustom();
        //Set Content Type
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String requestAsString = MapperUtil.getInstance().getMapper().writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestAsString, headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        try {
            return MapperUtil.getInstance().getMapper().readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
