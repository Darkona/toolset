package com.darkona.toolset.feign.retry;

import feign.RetryableException;
import feign.Retryer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class StaggeredRetryer implements Retryer {

    int level;
    int maxLevel;
    private int attempts;
    private Instant nextAttempt;
    private String name;
    private int maxAttempts;
    private int totalAttempts;

    @Autowired
    private RetryPropertiesProvider retryPropertiesProvider;

    private Map<Integer, RetryStage> backoffByLevel;

    public StaggeredRetryer(String name) {
        this.name = name;
        this.level = 0;
        this.attempts = 0;
        this.totalAttempts = 0;
    }

    @PostConstruct
    private void init(){
        backoffByLevel = retryPropertiesProvider.getForService(name);
        maxLevel = backoffByLevel.size() - 1;
        calculateMaxAttempts();
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if(attempts>=maxAttempts){
            throw e;
        }

    }

    @Override
    public Retryer clone() {
        var clone = new StaggeredRetryer(name);
        clone.level = level;
        clone.attempts = attempts;
        clone.totalAttempts = totalAttempts;
        clone.backoffByLevel = backoffByLevel;
        clone.maxLevel = maxLevel;
        clone.calculateMaxAttempts();
        return clone;
    }


    private void calculateMaxAttempts(){
        if(level < maxLevel){
            maxAttempts = (int) (backoffByLevel.get(level).maxBackOff().toSeconds() / backoffByLevel.get(level).backoff().toSeconds());
            attempts = 0;
        }
    }

    private void incrementAttempts(){
        attempts++;
        totalAttempts++;
        if(attempts >= maxAttempts){
            level++;
            calculateMaxAttempts();
        }
    }

    @Override
    public String toString(){
        return """
                RetryCall {
                    attempts= %d
                    nextAttempt= %s
                    name = %s
                    maxAttempts = %d
                    totalAttempts: %d
                    level: %d
                    maxLevel = %d
                }
                """.formatted(attempts, nextAttempt, name, maxAttempts, totalAttempts, level, maxLevel);
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean(name = "staggeredRetryConfiguration")
    public RetryPropertiesProvider noConfig(){
        return name -> Map.of(0, new RetryStage(Duration.ofSeconds(2), Duration.ofSeconds(20)));
    }


    interface RetryPropertiesProvider {
        Map<Integer, RetryStage> getForService(String name);
    }
}
