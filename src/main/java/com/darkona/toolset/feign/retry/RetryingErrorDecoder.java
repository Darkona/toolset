package com.darkona.toolset.feign.retry;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class RetryingErrorDecoder implements ErrorDecoder{


    private final ErrorDecoder delegate;

    public RetryingErrorDecoder(ErrorDecoder delegate) {
        this.delegate = delegate;
    }


    @Override
    public Exception decode(String methodKey, Response response) {

        if(response.body() == null || HttpStatus.valueOf(response.status()).is5xxServerError()){
            return new RetryableException(
                    response.status(),
                    "Retryable error: " + response.status(),
                    response.request().httpMethod(),
                    null,
                    3000l,
                    response.request()
            );
        }

        return delegate.decode(methodKey, response);
    }
}
