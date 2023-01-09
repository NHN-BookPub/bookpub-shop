package com.nhnacademy.bookpubshop.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mysql 접속 설정 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "mysql")
public class DataSourceConfig {
    private final KeyConfig keyConfig;

    private String url;
    private String userName;
    private String password;
    private Integer initialSize;
    private Integer maxTotal;
    private Integer minIdle;
    private Integer maxIdle;
    private Integer maxWait;

    /**
     * 데이터소스 설정을 빈으로 등록합니다.
     * @return 데이터소스를 리턴합니다.
     */
    @Bean
    public DataSource dataSource() throws UnrecoverableKeyException, CertificateException,
            KeyStoreException, IOException, NoSuchAlgorithmException,
            KeyManagementException {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        dataSource.setUrl(keyConfig.keyStore(url));
        log.error(keyConfig.keyStore(url));
        dataSource.setUsername(keyConfig.keyStore(userName));
        log.error(keyConfig.keyStore(userName));
        dataSource.setPassword(keyConfig.keyStore(password));
        log.error(keyConfig.keyStore(password));

        dataSource.setInitialSize(initialSize);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxIdle(maxIdle);

        dataSource.setMaxWaitMillis(maxWait);

        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        return dataSource;
    }

    public KeyConfig getKeyConfig() {
        return keyConfig;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }
}
