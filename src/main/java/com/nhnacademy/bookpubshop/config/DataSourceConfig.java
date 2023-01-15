package com.nhnacademy.bookpubshop.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * mysql 접속 설정 클래스.
 *
 * @author : 임태원, 유호철
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
     *
     * @return dataSource 데이터소스를 리턴합니다.
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        dataSource.setUrl(keyConfig.keyStore(url));
        dataSource.setUsername(keyConfig.keyStore(userName));
        dataSource.setPassword(keyConfig.keyStore(password));

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

    /**
     * 엔티티관리자에 어떤 datasource 를 쓸껀지 정해줄때 쓰임.
     *
     * @param dataSource 데이터소스.
     * @return LocalContainerEntityManagerFactoryBean 엔티티관리자.
     * @author : 유호철
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.nhnacademy.bookpubshop");
        emf.setJpaVendorAdapter(jpaVendorAdapters());
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    private JpaVendorAdapter jpaVendorAdapters() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    private Properties jpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.highlight_sql", "true");
        jpaProperties.setProperty("hibernate.format_sql", "true");

        return jpaProperties;
    }

    /**
     * jpa 를 사용하기 때문에 JpaTranscationManager 를 사용한다.
     *
     * @param entityManagerFactory 엔티티매니저 팩토리 주입.
     * @return PlatformTransactionManager tx 매니저를 어떤식으로 쓸지 반환.
     * @author : 유호철
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
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
