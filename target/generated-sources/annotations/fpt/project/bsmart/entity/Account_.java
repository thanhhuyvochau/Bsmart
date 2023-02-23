package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<Account, InfoFindTutorAccount> infoFindTutorAccounts;
	public static volatile ListAttribute<Account, StudentClass> studentClasses;
	public static volatile SingularAttribute<Account, Role> role;
	public static volatile ListAttribute<Account, RequestReply> requestReplies;
	public static volatile ListAttribute<Account, Comment> comments;
	public static volatile SingularAttribute<Account, Resource> resource;
	public static volatile SingularAttribute<Account, String> keycloakUserId;
	public static volatile ListAttribute<Account, Question> questions;
	public static volatile ListAttribute<Account, Class> teacherClass;
	public static volatile ListAttribute<Account, FileAttachment> fileAttachments;
	public static volatile ListAttribute<Account, Request> requests;
	public static volatile SingularAttribute<Account, Boolean> isActive;
	public static volatile ListAttribute<Account, Transaction> transactions;
	public static volatile SingularAttribute<Account, Boolean> isKeycloak;
	public static volatile SingularAttribute<Account, String> password;
	public static volatile SingularAttribute<Account, Integer> moodleUserId;
	public static volatile SingularAttribute<Account, AccountDetail> accountDetail;
	public static volatile ListAttribute<Account, Notifier> notifiers;
	public static volatile ListAttribute<Account, Vote> votes;
	public static volatile SingularAttribute<Account, Long> id;
	public static volatile ListAttribute<Account, StudentAnswer> studentAnswers;
	public static volatile ListAttribute<Account, FeedbackAccountLog> feedbackAccountLogs;
	public static volatile SingularAttribute<Account, String> username;

	public static final String INFO_FIND_TUTOR_ACCOUNTS = "infoFindTutorAccounts";
	public static final String STUDENT_CLASSES = "studentClasses";
	public static final String ROLE = "role";
	public static final String REQUEST_REPLIES = "requestReplies";
	public static final String COMMENTS = "comments";
	public static final String RESOURCE = "resource";
	public static final String KEYCLOAK_USER_ID = "keycloakUserId";
	public static final String QUESTIONS = "questions";
	public static final String TEACHER_CLASS = "teacherClass";
	public static final String FILE_ATTACHMENTS = "fileAttachments";
	public static final String REQUESTS = "requests";
	public static final String IS_ACTIVE = "isActive";
	public static final String TRANSACTIONS = "transactions";
	public static final String IS_KEYCLOAK = "isKeycloak";
	public static final String PASSWORD = "password";
	public static final String MOODLE_USER_ID = "moodleUserId";
	public static final String ACCOUNT_DETAIL = "accountDetail";
	public static final String NOTIFIERS = "notifiers";
	public static final String VOTES = "votes";
	public static final String ID = "id";
	public static final String STUDENT_ANSWERS = "studentAnswers";
	public static final String FEEDBACK_ACCOUNT_LOGS = "feedbackAccountLogs";
	public static final String USERNAME = "username";

}

