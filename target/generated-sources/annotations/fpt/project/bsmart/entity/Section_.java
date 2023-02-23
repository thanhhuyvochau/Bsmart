package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Section.class)
public abstract class Section_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Section, String> name;
	public static volatile SingularAttribute<Section, Long> moodleId;
	public static volatile SingularAttribute<Section, Long> id;
	public static volatile SingularAttribute<Section, Boolean> isVisible;
	public static volatile SingularAttribute<Section, Class> clazz;
	public static volatile ListAttribute<Section, Module> modules;
	public static volatile SingularAttribute<Section, ForumLesson> forumLesson;

	public static final String NAME = "name";
	public static final String MOODLE_ID = "moodleId";
	public static final String ID = "id";
	public static final String IS_VISIBLE = "isVisible";
	public static final String CLAZZ = "clazz";
	public static final String MODULES = "modules";
	public static final String FORUM_LESSON = "forumLesson";

}

