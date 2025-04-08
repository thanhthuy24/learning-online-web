package com.htt.elearning.course.pojo;

import com.htt.elearning.course.response.CourseResponseRedis;
import com.htt.elearning.course.service.CourseRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class CourseListerner {
    private static final Logger logger = LoggerFactory.getLogger(CourseListerner.class);
    private final CourseRedisService courseRedisService;

    @PrePersist
    public void prePersist(Course course) {
        logger.info("prePersist");
    }

    @PostPersist //save = persis
    public void postPersist(Course course) {
        // Update Redis cache
        logger.info("postPersist");
        courseRedisService.clear();
    }

    @PreUpdate
    public void preUpdate(Course course) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Course course) {
        // Update Redis cache
        logger.info("postUpdate");
        courseRedisService.clear();
    }

    @PreRemove
    public void preRemove(Course course) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(Course course) {
        // Update Redis cache
        logger.info("postRemove");
        courseRedisService.clear();
    }
}
