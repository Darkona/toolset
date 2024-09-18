package com.darkona.toolset.security;


import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class SSLContextProvider {


    private final SslBundles bundles;

    public SSLContext getSSLContext(String scope, @Nullable String bundleName) {
        if(bundleName == null){
            return getDefaultSSLContext(scope);
        }else{
            return getSSLContextFromBundle(scope, bundleName);
        }
    }

    public SSLContext getSSLContextFromBundle(String scope, String bundleName) {
        log.info("Creating SSLContext for scope: {} and bundle: {}", scope, bundleName);

        try {
            return bundles.getBundle(bundleName).createSslContext();
        } catch (Exception e) {
            log.warn("Failed to create SSLContext for scope: {} and bundle: {}, creating default context", scope, bundleName, e);
            return null;
        }
    }

    public SSLContext getDefaultSSLContext(String scope){
        log.info("Creating default SSLContext for scope: {}", scope);
        try {
            return SSLContext.getDefault();
        } catch (Exception e) {
            log.error("Failed to create default SSLContext for scope: {}", scope, e);
            return null;
        }
    }

}
