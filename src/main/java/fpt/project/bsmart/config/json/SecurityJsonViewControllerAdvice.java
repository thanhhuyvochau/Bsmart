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
public class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    public final static JsonRoleViewHolder jsonViewHolder = new JsonRoleViewHolder();

    public static class JsonRoleViewHolder {
        private EUserRole JSON_ROLE_VIEW = EUserRole.ANONYMOUS;

        public EUserRole getJsonRoleView() {
            return JSON_ROLE_VIEW;
        }

        public void setJsonRoleView(EUserRole jsonRoleView) {
            synchronized (this) {
                JSON_ROLE_VIEW = jsonRoleView;
            }
        }

        void resetState() {
            JSON_ROLE_VIEW = EUserRole.ANONYMOUS;
        }
    }

    @Override
    protected synchronized void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        synchronized (jsonViewHolder) {
            Map<EUserRole, Class> mapping = View.MAPPING;
            Class jsonViewClass = mapping.get(jsonViewHolder.getJsonRoleView());
            System.out.println("-------------RESPONSE FOR ROLE:" + jsonViewClass.getName() + "------------------");
            if (!jsonViewClass.getName().equals(View.Anonymous.class.getName())) {
                bodyContainer.setSerializationView(jsonViewClass);
            }
            jsonViewHolder.resetState();
        }

    }


}
