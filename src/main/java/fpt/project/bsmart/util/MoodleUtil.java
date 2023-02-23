package fpt.project.bsmart.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.Account;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.moodle.repository.MoodleUserRepository;
import fpt.project.bsmart.moodle.response.MoodleUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoodleUtil {
    private final MoodleUserRepository moodleUserRepository;

    public MoodleUtil(MoodleUserRepository moodleUserRepository) {
        this.moodleUserRepository = moodleUserRepository;
    }


    public MoodleUserResponse getMoodleUserIfExist(Account account) throws JsonProcessingException {
        return getMoodleUserIfExistByKeycloakId(account.getKeycloakUserId());
    }

    public MoodleUserResponse getMoodleUserIfExistByKeycloakId(String keycloakId) throws JsonProcessingException {
        List<MoodleUserResponse> moodleUsers = moodleUserRepository.getUserByUserName(keycloakId);
        if (moodleUsers.size() < 1) {
            StringBuilder message = new StringBuilder("Bạn phải cập nhật hồ sơ theo đường dẫn bên dưới nếu muốn sử dụng tiếp chức năng này: ")
                    .append(moodleUserRepository.getRootUrl());

            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(message.toString());
        } else if (moodleUsers.size() > 1) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Some errors happened, please contact admin for helping!");
        }
        return moodleUsers.get(0);
    }
}
