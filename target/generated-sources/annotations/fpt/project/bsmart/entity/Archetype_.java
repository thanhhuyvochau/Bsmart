package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Archetype.class)
public abstract class Archetype_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile ListAttribute<Archetype, ArchetypeTime> archetypeTimes;
	public static volatile SingularAttribute<Archetype, String> code;
	public static volatile SingularAttribute<Archetype, String> name;
	public static volatile SingularAttribute<Archetype, Long> id;
	public static volatile SingularAttribute<Archetype, Long> createdByTeacherId;

	public static final String ARCHETYPE_TIMES = "archetypeTimes";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String CREATED_BY_TEACHER_ID = "createdByTeacherId";

}

