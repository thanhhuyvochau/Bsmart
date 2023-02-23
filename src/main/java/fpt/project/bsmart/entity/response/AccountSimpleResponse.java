package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.dto.RoleDto;

public class AccountSimpleResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private RoleDto role;
    private GenderResponse gender;
    private String avatar;
    private EAccountDetailStatus status;

    private String level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public GenderResponse getGender() {
        return gender;
    }

    public void setGender(GenderResponse gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public EAccountDetailStatus getStatus() {
        return status;
    }

    public void setStatus(EAccountDetailStatus status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
