package fpt.project.bsmart.config.notify;

import fpt.project.bsmart.entity.common.ApiException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource(value = "classpath:notification-message.yaml", factory = YamlPropertySourceFactory.class)
public class NotificationProperties {
    private List<NotifyDetail> notify = new ArrayList<>();

    public List<NotifyDetail> getNotify() {
        return notify;
    }

    public void setNotify(List<NotifyDetail> notify) {
        this.notify = notify;
    }

    public Map<String, NotifyDetail> getNotifyAsMap() {
        return notify.stream().collect(Collectors.toMap(NotifyDetail::getCode, Function.identity()));
    }

    public static class NotifyDetail {
        private String title;
        private String template;
        private String entity;
        private String code;

        public String getTitle() {
            if (title == null)
                throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Notification config occurs null attribute (title).");
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTemplate() {

            if (template == null)
                throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Notification config occurs null attribute (content).");
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getEntity() {
            if (entity == null)
                throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Notification config occurs null attribute (entity).");
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getCode() {
            if (code == null)
                throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Notification config occurs null attribute (code).");
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
