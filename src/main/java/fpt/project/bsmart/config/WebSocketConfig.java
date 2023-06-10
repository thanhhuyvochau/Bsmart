package fpt.project.bsmart.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/receive");
        // we enable a broker with two destinations /receive

        config.setApplicationDestinationPrefixes("/ws");
        // set the application destination i.e /ws , which will provide us to send messages to the application.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // giúp chúng ta định nghĩa những endpoint mà client sẽ sử dụng để gọi và kết nối tới WebSocket.
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*");
        //không phải tất cả các trình duyệt đều hỗ trợ WebSocket => Dùng SockJS
        registry.addEndpoint("/ws").withSockJS();

    }


}
