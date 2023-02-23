package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Module.class)
public abstract class Module_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<Module, String> name;
	public static volatile SingularAttribute<Module, Section> section;
	public static volatile SingularAttribute<Module, Integer> moodleId;
	public static volatile SingularAttribute<Module, Long> id;
	public static volatile SingularAttribute<Module, EModuleType> type;
	public static volatile SingularAttribute<Module, String> url;

	public static final String NAME = "name";
	public static final String SECTION = "section";
	public static final String MOODLE_ID = "moodleId";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String URL = "url";

}

