package com.shehab.udms.config;

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
        // '/topic' দিয়ে শুরু হওয়া মেসেজগুলো ক্লায়েন্ট (টিচার) লিসেন করবে
        config.enableSimpleBroker("/topic");    // server → client
        config.setApplicationDestinationPrefixes("/app");        // client → server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ফ্রন্টএন্ড এই URL দিয়ে কানেক্ট হবে
        registry.addEndpoint("/ws-attendance")
                .setAllowedOrigins("http://localhost:3000") // আপনার ফ্রন্টএন্ড URL দিন
                .withSockJS();
    }

}
