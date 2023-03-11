package fpt.project.bsmart.util.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.util.MapperUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCaller {
    private final RestTemplate restTemplate;

    public RestCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <RequestType, ResponseType> ResponseType get(String uri, RequestType request, TypeReference<ResponseType> typeReference) throws JsonProcessingException {
        //Set Content Type
        String requestAsString = MapperUtil.getInstance().getMapper().writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestAsString);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        try {
            return MapperUtil.getInstance().getMapper().readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <RequestType, ResponseType> ResponseType post(String uri, RequestType request, TypeReference<ResponseType> typeReference) throws JsonProcessingException {
        //Set Content Type
        String requestAsString = MapperUtil.getInstance().getMapper().writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestAsString);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        try {
            return MapperUtil.getInstance().getMapper().readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
