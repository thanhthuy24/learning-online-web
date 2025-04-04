package com.htt.elearning.sentiment;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SentimentServiceImpl implements SentimentService {
    private final RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    @Override
    public String analyzeSentiment(String comment) throws JSONException {
        Map<String, String> request = new HashMap<>();
        request.put("comment", comment);

        String response = restTemplate.postForObject(pythonApiUrl + "/analyze", request, String.class);
        JSONObject jsonResponse = new JSONObject(response);

        double sentimentScore = jsonResponse.getDouble("sentiment_score");
        if (sentimentScore > 0.05) return "Positive";
        if (sentimentScore < -0.05) return "Negative";
        return "Neutral";
//        return getSentimentLabel(sentimentScore);
    }

    @Override
    public String analyzeSentimentRating(String comment) throws JSONException {
        Map<String, String> request = new HashMap<>();
        request.put("comment", comment);

        String response = restTemplate.postForObject(pythonApiUrl + "/analyze-rating", request, String.class);
        JSONObject jsonResponse = new JSONObject(response);

        double sentimentScore = jsonResponse.getDouble("sentiment_score");
        if (sentimentScore > 0.05) return "Positive";
        if (sentimentScore < -0.05) return "Negative";
        return "Neutral";
    }
}
