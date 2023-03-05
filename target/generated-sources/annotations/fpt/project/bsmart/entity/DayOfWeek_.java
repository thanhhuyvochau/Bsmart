package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DayOfWeek.class)
public abstract class DayOfWeek_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<DayOfWeek, EDayOfWeekCode> code;
	public static volatile SingularAttribute<DayOfWeek, String> name;
	public static volatile SingularAttribute<DayOfWeek, Long> id;

	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ID = "id";

}

