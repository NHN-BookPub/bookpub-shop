package com.nhnacademy.bookpubshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * object storage properties 처리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Configuration
@ConfigurationProperties(prefix = "storage")
@RequiredArgsConstructor
public class ObjectStorageProperties {
    private final KeyConfig keyConfig;
    private String url;
    private String username;
    private String containerName;
    private String tenantId;
    private String password;
    private String identity;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {

        this.url = keyConfig.keyStore(url);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = keyConfig.keyStore(username);
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {

        this.containerName = keyConfig.keyStore(containerName);
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {

        this.tenantId = keyConfig.keyStore(tenantId);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = keyConfig.keyStore(password);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = keyConfig.keyStore(identity);
    }
}
