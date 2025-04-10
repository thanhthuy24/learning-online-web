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

    public void sendAssignmentCreateEvent(AssignmentCreateEvent assignmentCreateEvent, String token) {
        log.info("Sending create new assignment successfully");

        // Tạo message và thêm header "Authorization"
        Message<AssignmentCreateEvent> message = MessageBuilder
                .withPayload(assignmentCreateEvent)
                .setHeader("Authorization", token)  // Thêm header Authorization
                .setHeader(TOPIC, "assignment-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
