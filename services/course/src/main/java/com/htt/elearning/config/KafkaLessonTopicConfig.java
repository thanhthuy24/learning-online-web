package com.htt.elearning.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaLessonTopicConfig {
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("lesson-topic")
                .build();
    }
}
