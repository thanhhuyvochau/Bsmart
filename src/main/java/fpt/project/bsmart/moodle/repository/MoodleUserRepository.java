package fpt.project.bsmart.moodle.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.moodle.config.MoodleCaller;
import fpt.project.bsmart.moodle.request.GetUserByFieldRequest;
import fpt.project.bsmart.moodle.response.MoodleUserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MoodleUserRepository extends MoodleBaseRepository {

    public MoodleUserRepository(MoodleCaller moodleCaller) {
        super(moodleCaller);
    }

    // Username of moodle user is id of user in Keycloak
    public List<MoodleUserResponse> getUserByUserName(String username) throws JsonProcessingException { // id in Keycloak
        GetUserByFieldRequest request = new GetUserByFieldRequest();
        request.getValues().add(username);
        TypeReference<List<MoodleUserResponse>> typeReference = new TypeReference<List<MoodleUserResponse>>() {
        };
        return moodleCaller.post(getGetUserUrl(), request, typeReference);
    }
}
