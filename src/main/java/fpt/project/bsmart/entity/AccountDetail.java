package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.common.EGenderType;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "account_detail")
public class AccountDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_generator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthDay")
    private Instant birthDay;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;


    @ManyToOne
    @JoinColumn(name = "voice_id")
    private Voice voice;

    @Column(name = "current_address")
    private String currentAddress;


    @Column(name = "id_card")
    private String idCard;


    @Column(name = "school_name")
    private String trainingSchoolName;

    @Column(name = "majors")
    private String majors;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAccountDetailStatus status;

    @Column(name = "level")
    private String level;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private EGenderType gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "accountDetail", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AccountDetailSubject> accountDetailSubjects = new ArrayList<>();


    @OneToMany(mappedBy = "accountDetail", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AccountDetailClassLevel> accountDetailClassLevels = new ArrayList<>();


    @OneToMany(mappedBy = "accountDetail", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Resource> resources;

    @OneToMany(mappedBy = "accountDetail", cascade = CascadeType.ALL)
    private List<FeedbackAccountLog> feedbackAccountLogs = new ArrayList<>();


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTrainingSchoolName() {
        return trainingSchoolName;
    }

    public void setTrainingSchoolName(String trainingSchoolName) {
        this.trainingSchoolName = trainingSchoolName;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<AccountDetailSubject> getAccountDetailSubjects() {
        return accountDetailSubjects;
    }

    public void setAccountDetailSubjects(List<AccountDetailSubject> accountDetailSubjects) {
        this.accountDetailSubjects = accountDetailSubjects;
    }

    public List<AccountDetailClassLevel> getAccountDetailClassLevels() {
        return accountDetailClassLevels;
    }

    public void setAccountDetailClassLevels(List<AccountDetailClassLevel> accountDetailClassLevels) {
        this.accountDetailClassLevels = accountDetailClassLevels;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }

    public List<FeedbackAccountLog> getFeedbackAccountLogs() {
        return feedbackAccountLogs;
    }

    public void setFeedbackAccountLogs(List<FeedbackAccountLog> feedbackAccountLogs) {
        this.feedbackAccountLogs = feedbackAccountLogs;
    }
}
