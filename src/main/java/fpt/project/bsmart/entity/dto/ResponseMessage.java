package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.ENotificationEntity;
import fpt.project.bsmart.entity.constant.ENotificationType;

public class ResponseMessage extends BaseEntity {
    private Long id;
    private String viTitle;
    private String viContent;
    private ENotificationType type;
    private ENotificationEntity entity;
    private Long entityId;
    private boolean read = false;

    public ResponseMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRead() {
        return read;
    }

    public String getViTitle() {
        return viTitle;
    }

    public void setViTitle(String viTitle) {
        this.viTitle = viTitle;
    }

    public String getViContent() {
        return viContent;
    }

    public void setViContent(String viContent) {
        this.viContent = viContent;
    }

    public ENotificationType getType() {
        return type;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public ENotificationEntity getEntity() {
        return entity;
    }

    public void setEntity(ENotificationEntity entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
