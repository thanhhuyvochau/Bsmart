package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ESubjectCode;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Subject.class)
public abstract class Subject_ {

	public static volatile SingularAttribute<Subject, Forum> forum;
	public static volatile ListAttribute<Subject, Course> courses;
	public static volatile SingularAttribute<Subject, Long> categoryMoodleId;
	public static volatile ListAttribute<Subject, InfoFindTutorSubject> infoFindTutorSubjects;
	public static volatile SingularAttribute<Subject, ESubjectCode> code;
	public static volatile ListAttribute<Subject, AccountDetailSubject> accountDetailSubjects;
	public static volatile SingularAttribute<Subject, String> name;
	public static volatile SingularAttribute<Subject, Long> id;

	public static final String FORUM = "forum";
	public static final String COURSES = "courses";
	public static final String CATEGORY_MOODLE_ID = "categoryMoodleId";
	public static final String INFO_FIND_TUTOR_SUBJECTS = "infoFindTutorSubjects";
	public static final String CODE = "code";
	public static final String ACCOUNT_DETAIL_SUBJECTS = "accountDetailSubjects";
	public static final String NAME = "name";
	public static final String ID = "id";

}

