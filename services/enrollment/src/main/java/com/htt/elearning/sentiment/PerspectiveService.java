package com.htt.elearning.sentiment;

public interface PerspectiveService {
    double analyzeComment(String comment);
    String analyzeCommentSentiment(String comment);
}
