package fpt.project.bsmart.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {
    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLocalMessage(String messageKey) {
        if (messageKey == null) {
            return null;
        }
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());

    }
    public String getVietnameseMessage(String messageKey) {
        if (messageKey == null) {
            return null;
        }
        return messageSource.getMessage(messageKey, null, Locale.forLanguageTag("vi"));
    }

    public String getEnglishMessage(String messageKey) {
        if (messageKey == null) {
            return null;
        }
        return messageSource.getMessage(messageKey, null, Locale.ENGLISH);
    }


}
