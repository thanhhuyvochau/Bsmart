package fpt.project.bsmart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.constant.EGenderType;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user")


public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;


    @Column(name = "birthday")
    private Instant birthday;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private boolean status = false;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles = new ArrayList<>();
    @Column(name = "linkedin_Link")
    private String linkedinLink;
    @Column(name = "facebook_link")
    private String facebookLink;
    @Column(name = "website")
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private EGenderType gender;
    @JsonIgnore
    @Column(name = "intro_point")
    private Double point;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    private List<UserImage> userImages = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Wallet wallet = new Wallet();

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private MentorProfile mentorProfile;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Cart cart;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    private List<Order> order = new ArrayList<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "user")
    private List<ReferralCode> referralCodes = new ArrayList<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
    private List<StudentClass> studentClasses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifier> notifiers = new ArrayList<>();

    @JsonIgnore
    @Column(name = "is_verified")
    private boolean isVerified = false;

    @JsonIgnore
    @Column(name = "provider")
    private String provider;

    @JsonIgnore
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<UserImage> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<UserImage> userImages) {
        this.userImages = userImages;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public MentorProfile getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfile mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public List<ReferralCode> getReferralCodes() {
        return referralCodes;
    }

    public void setReferralCodes(List<ReferralCode> referralCodes) {
        this.referralCodes = referralCodes;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Notifier> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }
}
