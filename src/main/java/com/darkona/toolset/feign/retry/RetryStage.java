package com.darkona.toolset.feign.retry;

import java.time.Duration;

public record RetryStage(Duration backoff, Duration maxBackOff) {
}
