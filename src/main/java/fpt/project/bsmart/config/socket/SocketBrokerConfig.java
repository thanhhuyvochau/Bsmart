package fpt.project.bsmart.config.socket;

//
//
//@EnableWebSocketMessageBroker
//@Configuration
//public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry
//                .addEndpoint("/websocket").setAllowedOriginPatterns("*")
//                .setHandshakeHandler(new ClientHandshakeHandler())
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic", "/queue");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//}
