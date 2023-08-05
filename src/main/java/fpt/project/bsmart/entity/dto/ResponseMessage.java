package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.ENotificationEntity;
import fpt.project.bsmart.entity.constant.ENotificationType;

public class ResponseMessage {
    private String viTitle;
    private String viContent;
    private ENotificationType type;
    private ENotificationEntity entity;
    private Long entityId;
    private boolean isRead = false;


    public ResponseMessage(String viTitle, String viContent, ENotificationType type, ENotificationEntity entity, Long entityId, boolean isRead) {
        this.viTitle = viTitle;
        this.viContent = viContent;
        this.type = type;
        this.entity = entity;
        this.entityId = entityId;
        this.isRead = isRead;
    }

    public ResponseMessage() {
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

    public boolean getIsRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
