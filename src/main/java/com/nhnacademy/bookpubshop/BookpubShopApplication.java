package com.nhnacademy.bookpubshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Book-pub 시작.
 *
 * @author 유호철
 * @author 임태원
 * @author 여운석
 * @author 정유진
 * @author 김서현
 * @author 박경서
 */
@EnableJpaAuditing
@EnableRedisHttpSession
@ConfigurationPropertiesScan
@SpringBootApplication
public class BookpubShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookpubShopApplication.class, args);
    }

}
