package fpt.project.bsmart.entity.request;

public class StudentAttemptQuizRequest {
    private Long quizId;
    private String password;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
