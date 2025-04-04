package com.htt.elearning.sentiment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.HttpEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerspectiveServiceImpl implements PerspectiveService {
//    @Value("${perspective.api.key}")
//    private String apiKey;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public double analyzeComment(String comment) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo JSON body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comment", comment);

        // Định dạng request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Gửi request
        ResponseEntity<Map> response = restTemplate.exchange(pythonApiUrl+"/predict", HttpMethod.POST,
                entity, Map.class);

        // Xử lý phản hồi
        if (response.getStatusCode() == HttpStatus.OK) {
            Double score = (Double) response.getBody().get("score");
            return score;
        }
        return 0;
    }

    @Override
    public String analyzeCommentSentiment(String comment) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo JSON body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comment", comment);

        // Định dạng request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Gửi request
        ResponseEntity<Map> response = restTemplate.exchange(pythonApiUrl+"/predict", HttpMethod.POST, entity, Map.class);

        // Xử lý phản hồi
        if (response.getStatusCode() == HttpStatus.OK) {
            String label = (String) response.getBody().get("label");
            return label;
        }
        return null;
    }
}
