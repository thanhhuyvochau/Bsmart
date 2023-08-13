package fpt.project.bsmart.entity.response.mentor;

import fpt.project.bsmart.entity.dto.UserDto;

import java.util.List;

public class MentorEditProfileDetailResponse {
    private Long id ;

    private UserDto userDtoEdit ;

    private UserDto userDtoOrigin ;

    private List<String> differentFields  ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDtoEdit() {
        return userDtoEdit;
    }

    public void setUserDtoEdit(UserDto userDtoEdit) {
        this.userDtoEdit = userDtoEdit;
    }

    public UserDto getUserDtoOrigin() {
        return userDtoOrigin;
    }

    public void setUserDtoOrigin(UserDto userDtoOrigin) {
        this.userDtoOrigin = userDtoOrigin;
    }

    public List<String> getDifferentFields() {
        return differentFields;
    }

    public void setDifferentFields(List<String> differentFields) {
        this.differentFields = differentFields;
    }
}
