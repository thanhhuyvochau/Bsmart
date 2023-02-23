package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeedBack.class)
public abstract class FeedBack_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<FeedBack, StudentClassKey> studentClassKeyId;
	public static volatile SingularAttribute<FeedBack, Long> teacherId;
	public static volatile SingularAttribute<FeedBack, Integer> pointEvaluation;
	public static volatile SingularAttribute<FeedBack, Long> id;
	public static volatile SingularAttribute<FeedBack, String> content;

	public static final String STUDENT_CLASS_KEY_ID = "studentClassKeyId";
	public static final String TEACHER_ID = "teacherId";
	public static final String POINT_EVALUATION = "pointEvaluation";
	public static final String ID = "id";
	public static final String CONTENT = "content";

}

