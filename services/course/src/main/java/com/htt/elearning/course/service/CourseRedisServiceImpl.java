package com.htt.elearning.course.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.CourseResponseRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRedisServiceImpl implements CourseRedisService {
//    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
    @Value("${spring.data.redis.use-redis-cache}")
    private boolean useRedisCache;

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        String key = String.format("all_courses:%s:%d:%d:%d:%s", keyword, categoryId, pageNumber, pageSize, sortDirection);
        return key;
    }

    @Override
    public List<CourseResponseRedis> getAllCourses(PageRequest pageRequest, Long categoryId, String keyword) throws JsonProcessingException {
        if(useRedisCache == false) {
            return null;
        }

        String key = this.getKeyFrom(keyword, categoryId, pageRequest);

        String json = (String) redisTemplate.opsForValue().get(key);
        List<CourseResponseRedis> courseResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<CourseResponseRedis>>() {})
                        : null;

        return courseResponses;
    }

    @Override
    public void saveAllCourses(List<CourseResponseRedis> courses, PageRequest pageRequest, Long categoryId, String keyword) throws JsonProcessingException {
        String key = getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(courses);
        redisTemplate.opsForValue().set(key, json);
    }
}
