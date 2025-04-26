package com.htt.elearning.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 500) {
            return new RetryableException(
                    response.status(),
                    "Server error: " + response.reason(),
                    response.request().httpMethod(),
                    (Long) null,
                    response.request()
            );
        }
        return defaultDecoder.decode(methodKey, response);
    }
}

