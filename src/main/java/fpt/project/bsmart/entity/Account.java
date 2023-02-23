package fpt.project.bsmart.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "account")
public class Account extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_generator")
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountDetail accountDetail;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClass> studentClasses = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Class> teacherClass = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestReply> requestReplies = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    @Column(name = "is_keycloak")
    private Boolean isKeycloak;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<InfoFindTutorAccount> infoFindTutorAccounts = new ArrayList<>();


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<FileAttachment> fileAttachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Resource resource;

    @OneToMany(mappedBy = "account")
    private List<Vote> votes = new ArrayList<>();
    private String keycloakUserId;
    private Integer moodleUserId;
    @OneToMany(mappedBy = "notifier", cascade = CascadeType.PERSIST)
    private List<Notifier> notifiers = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<FeedbackAccountLog> feedbackAccountLogs = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        this.isActive = active;
    }

    public List<InfoFindTutorAccount> getInfoFindTutorAccounts() {
        return infoFindTutorAccounts;
    }

    public void setInfoFindTutorAccounts(List<InfoFindTutorAccount> infoFindTutorAccounts) {
        this.infoFindTutorAccounts = infoFindTutorAccounts;
    }

    public List<RequestReply> getRequestReplies() {
        return requestReplies;
    }

    public void setRequestReplies(List<RequestReply> requestReplies) {
        this.requestReplies = requestReplies;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }


    public Boolean getKeycloak() {
        return isKeycloak;
    }

    public void setKeycloak(Boolean keycloak) {
        isKeycloak = keycloak;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }


    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;

    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public List<Class> getTeacherClass() {
        return teacherClass;
    }

    public void setTeacherClass(List<Class> teacherClass) {
        this.teacherClass = teacherClass;
    }


    public List<FileAttachment> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<FileAttachment> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(username, account.username) && Objects.equals(password, account.password) && Objects.equals(isActive, account.isActive) && Objects.equals(role, account.role) && Objects.equals(transactions, account.transactions) && Objects.equals(studentClasses, account.studentClasses) && Objects.equals(teacherClass, account.teacherClass) && Objects.equals(requests, account.requests) && Objects.equals(studentAnswers, account.studentAnswers) && Objects.equals(questions, account.questions) && Objects.equals(comments, account.comments) && Objects.equals(accountDetail, account.accountDetail) && Objects.equals(infoFindTutorAccounts, account.infoFindTutorAccounts) && Objects.equals(fileAttachments, account.fileAttachments) && Objects.equals(resource, account.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isActive, role, transactions, studentClasses, teacherClass, requests, studentAnswers, questions, comments, accountDetail, infoFindTutorAccounts, fileAttachments, resource);
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public Integer getMoodleUserId() {
        return moodleUserId;
    }

    public void setMoodleUserId(Integer moodleUserId) {
        this.moodleUserId = moodleUserId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Notifier> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public List<FeedbackAccountLog> getFeedbackAccountLogs() {
        return feedbackAccountLogs;
    }

    public void setFeedbackAccountLogs(List<FeedbackAccountLog> feedbackAccountLogs) {
        this.feedbackAccountLogs = feedbackAccountLogs;
    }
}
