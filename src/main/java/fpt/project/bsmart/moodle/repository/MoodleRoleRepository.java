package fpt.project.bsmart.moodle.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.moodle.config.Caller;
import fpt.project.bsmart.moodle.response.MoodleRoleResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoodleRoleRepository extends MoodleBaseRepository {


    public MoodleRoleRepository(Caller caller) {
        super(caller);
    }

    public List<MoodleRoleResponse> getRoles() throws JsonProcessingException {
        TypeReference<List<MoodleRoleResponse>> typeReference = new TypeReference<List<MoodleRoleResponse>>() {
        };
        return caller.get(getGetRolesUrl(), "", typeReference);
    }
}
