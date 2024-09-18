package com.darkona.toolset.feign.retry;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "feign")
@EnableConfigurationProperties
public class StaggeredRetryConfiguration {

    private final Duration maxPossibleMaxBackOff = Duration.ofDays(14);
    private final Duration maxPossibleBackOff = Duration.ofDays(2);
    private static final int DEFAULT_BACKOFF = 2;
    private static final int DEFAULT_MAX_BACKOFF = 20;
    private static final TimeUnit DEFAULT_BACKOFF_UNIT = TimeUnit.SECONDS;
    private static final TimeUnit DEFAULT_MAX_BACKOFF_UNIT = TimeUnit.SECONDS;

    private final List<RetryConfig> defaultConfig = List.of(new RetryConfig(0, 2, TimeUnit.SECONDS, 20, TimeUnit.SECONDS));
    private Map<String, List<RetryConfig>> retry = new HashMap<>();


    public void setRetry(Map<String, List<RetryConfig>> loadedConfig) {

        retry.putAll(loadedConfig);


        if (retry.isEmpty()) {
            retry.put("default", defaultConfig);
        }

        for (var entry : retry.entrySet()) {
            for (var config : entry.getValue()) {
                sanitize(entry.getKey(), config);
            }
        }

    }


    private void sanitize(String key, RetryConfig config) {

        if (config.backOff < 0) {
            log.warn("Backoff for {} is less than 0. Setting backoff to {} seconds", key, DEFAULT_BACKOFF);
            config.setBackOff(DEFAULT_BACKOFF);
            config.setBackOffUnit(DEFAULT_BACKOFF_UNIT);
        }
        if (config.backOff < 0) {
            log.warn("Backoff for {} is less than 0. Setting backoff to {} seconds", key, DEFAULT_BACKOFF);
            config.setBackOff(DEFAULT_BACKOFF);
            config.setMaxBackOffUnit(DEFAULT_MAX_BACKOFF_UNIT);
        }

        var backOff = Duration.of(config.backOff, config.backOffUnit.toChronoUnit());
        var maxBackOff = Duration.of(config.maxBackOff, config.maxBackOffUnit.toChronoUnit());

        if(backOff.compareTo(maxPossibleBackOff) > 0){
            log.warn("Backoff for {} is greater than the maximum possible backoff of {}. Setting backoff to {} seconds",
                    key, maxPossibleBackOff, maxPossibleBackOff.getSeconds());

            config.setBackOff((int) maxPossibleBackOff.getSeconds());
            config.setBackOffUnit(TimeUnit.SECONDS);
        }
        if(maxBackOff.compareTo(maxPossibleMaxBackOff) > 0){
            log.warn("MaxBackoff for {} is greater than the maximum possible maxbackoff of {}. Setting maxbackoff to {} seconds",
                    key, maxPossibleMaxBackOff, maxPossibleMaxBackOff.getSeconds());

            config.setMaxBackOff((int) maxPossibleMaxBackOff.getSeconds());
            config.setMaxBackOffUnit(TimeUnit.SECONDS);
        }
    }

    public Map<Integer, RetryStage> getForService(String name){

        var key = retry.containsKey(name) ? name : "default";
        var retryConfig = retry.get(key);

        var stages = new HashMap<Integer, RetryStage>();

        for(var config : retry.get(key)){
            stages.put(config.index, new RetryStage(
                    Duration.of(config.backOff, config.backOffUnit.toChronoUnit()),
                    Duration.of(config.maxBackOff, config.maxBackOffUnit.toChronoUnit())
                    ));
        }

        return stages;
    }


    @Data
    @AllArgsConstructor
    static final class RetryConfig {
        private int index;
        private int backOff;
        private TimeUnit backOffUnit;
        private int maxBackOff;
        private TimeUnit maxBackOffUnit;
    }

    @Bean
    @ConditionalOnBean(name = "staggeredRetryConfiguration")
    public StaggeredRetryer.RetryPropertiesProvider retryPropertiesProvider(StaggeredRetryConfiguration config){
        return config::getForService;
    }

}
