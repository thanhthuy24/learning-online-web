package com.htt.elearning.sentiment;

import org.json.JSONException;

public interface SentimentService {
    String analyzeSentiment(String comment) throws JSONException;
    String analyzeSentimentRating(String comment) throws JSONException;
}
