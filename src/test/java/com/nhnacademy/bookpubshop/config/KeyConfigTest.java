package com.nhnacademy.bookpubshop.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.nhnacademy.bookpubshop.config.KeyConfig;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/

@ExtendWith(SpringExtension.class)
@Import(KeyConfig.class)
class KeyConfigTest {
    @Autowired
    KeyConfig keyConfig;

    @Test
    void keyStore() throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        assertThat(keyConfig.keyStore("14d5e60c88194b5191e081e6f89825a6")).isEqualTo("3wX.DPUpjpdSn@d.");
    }
}