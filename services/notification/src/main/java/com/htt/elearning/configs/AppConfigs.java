package com.htt.elearning.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigs {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
