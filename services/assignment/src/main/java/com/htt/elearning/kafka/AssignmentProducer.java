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
public class AssignmentProducer {
    private final KafkaTemplate<String, AssignmentCreateEvent> kafkaTemplate;

    public void sendLessonCreateEvent(AssignmentCreateEvent assignmentCreateEvent) {
        log.info("Sending create new assignment successfully");
        Message<AssignmentCreateEvent> message = MessageBuilder
                .withPayload(assignmentCreateEvent)
                .setHeader(TOPIC, "assignments-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
