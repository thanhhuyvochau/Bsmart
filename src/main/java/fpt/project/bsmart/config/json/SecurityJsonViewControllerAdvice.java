package fpt.project.bsmart.config.json;

import fpt.project.bsmart.entity.constant.EUserRole;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Map;

@RestControllerAdvice
public
class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    private static EUserRole JSON_ROLE_VIEW = EUserRole.ANONYMOUS;

    public static synchronized void setJsonRoleView(EUserRole userRole) {
        JSON_ROLE_VIEW = userRole;
    }

    @Override
    protected synchronized void beforeBodyWriteInternal(

            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        synchronized (JSON_ROLE_VIEW) {
            Map<EUserRole, Class> mapping = View.MAPPING;
            Class jsonViewClass = mapping.get(JSON_ROLE_VIEW);
            bodyContainer.setSerializationView(jsonViewClass);
        }
    }
}
