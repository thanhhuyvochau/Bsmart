package fpt.project.bsmart.entity.response.mentor;

import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorSkillDto;
import fpt.project.bsmart.entity.dto.UserDto;

import java.time.Instant;
import java.util.List;

public class MentorEditProfileResponse {
    private Long id ;

    private UserDto userDto ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
