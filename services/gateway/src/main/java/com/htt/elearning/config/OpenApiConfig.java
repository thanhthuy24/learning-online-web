package com.htt.elearning.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "secure")
public class OpenApiConfig {

    private List<String> openPathsGet;
    private List<String> openPathsPost;

    // Getters và Setters
    public List<String> getOpenPathsGet() {
        return openPathsGet;
    }

    public void setOpenPathsGet(List<String> openPathsGet) {
        this.openPathsGet = openPathsGet;
    }

    public List<String> openPathsPost() {
        return openPathsPost;
    }

    public void setOpenPathsPost(List<String> openPathsPost) {
        this.openPathsPost = openPathsPost;
    }
}
