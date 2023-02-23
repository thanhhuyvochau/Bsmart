package fpt.project.bsmart.entity.request;


public class CreateQuestionRequest {
    private String content;
    private String title;
    private Long forumId;
    private Long forumLessonId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getForumLessonId() {
        return forumLessonId;
    }

    public void setForumLessonId(Long forumLessonId) {
        this.forumLessonId = forumLessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
