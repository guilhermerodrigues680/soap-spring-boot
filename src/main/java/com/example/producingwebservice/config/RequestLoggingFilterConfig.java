package com.example.producingwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(false);
        filter.setIncludeHeaders(false);
        filter.setIncludePayload(true);
        filter.setIncludeQueryString(true);
        filter.setMaxPayloadLength(10000);
        filter.setBeforeMessagePrefix("Request    : [");
        filter.setBeforeMessageSuffix("]");
        filter.setAfterMessagePrefix("Request end: [");
        filter.setAfterMessageSuffix("]");

        return filter;

    }
}
