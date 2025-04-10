package com.htt.elearning.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonProducer {
    private final KafkaTemplate<String, LessonCreateEvent> kafkaTemplate;

    public void sendLessonCreateEvent(LessonCreateEvent lessonCreateEvent, String token) {
        log.info("Sending create new lesson successfully");
        Message<LessonCreateEvent> message = MessageBuilder
                .withPayload(lessonCreateEvent)
                .setHeader("Authorization", token)
                .setHeader(TOPIC, "lesson-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
