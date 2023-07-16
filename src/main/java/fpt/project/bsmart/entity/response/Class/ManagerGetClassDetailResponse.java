package fpt.project.bsmart.entity.response.Class;

import fpt.project.bsmart.entity.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class ManagerGetClassDetailResponse extends MentorGetClassDetailResponse{
    private UserDto mentor;
    private List<UserDto> students = new ArrayList<>();

    public UserDto getMentor() {
        return mentor;
    }

    public void setMentor(UserDto mentor) {
        this.mentor = mentor;
    }

    public List<UserDto> getStudents() {
        return students;
    }

    public void setStudents(List<UserDto> students) {
        this.students = students;
    }
}
