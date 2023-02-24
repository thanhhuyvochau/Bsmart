package fpt.project.bsmart.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FileAttachment.class)
public abstract class FileAttachment_ extends fpt.project.bsmart.entity.BaseEntity_ {

	public static volatile SingularAttribute<FileAttachment, String> name;
	public static volatile SingularAttribute<FileAttachment, Long> id;
	public static volatile SingularAttribute<FileAttachment, String> url;
	public static volatile SingularAttribute<FileAttachment, EFileType> fileType;
	public static volatile SingularAttribute<FileAttachment, User> account;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String URL = "url";
	public static final String FILE_TYPE = "fileType";
	public static final String ACCOUNT = "account";

}

