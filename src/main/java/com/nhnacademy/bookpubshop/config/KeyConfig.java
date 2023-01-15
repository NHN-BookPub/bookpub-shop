package com.nhnacademy.bookpubshop.config;


import com.nhnacademy.bookpubshop.dto.KeyResponseDto;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 키 매니저를 사용하기위한 Config 입니다.
 *
 * @author : 유호철, 임태원
 * @since : 1.0
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "keymanager")
public class KeyConfig {

    private String password;
    private String appKey;
    private String url;
    private String path;

    /**
     * KeyStore 연결 클래스.
     *
     * @param keyId key 접속 아이디.
     * @return api 통신을 통해 교환받은 디코딩 값.
     * @throws KeyStoreException         keyStore 에러
     * @throws IOException               파일입출력 에러
     * @throws CertificateException      인증 에러
     * @throws NoSuchAlgorithmException  암호화 알고리즘 찾을수없을 때 나는 에러.
     * @throws UnrecoverableKeyException 키를 복호화 할 수 없을 때 나는 에러.
     * @throws KeyManagementException    모든 조작에 대한 일반적인 키 에러.
     */
    public String keyStore(String keyId) throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");

        ClassPathResource resource = new ClassPathResource("book-pub.p12");
        clientStore.load(new FileInputStream(resource.getFile()), password.toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .setProtocol("TLS")
                .loadKeyMaterial(clientStore, password.toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path(path)
                .encode()
                .build()
                .expand(appKey, keyId)
                .toUri();
        return Objects.requireNonNull(restTemplate.exchange(uri,
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                KeyResponseDto.class)
                        .getBody())
                .getBody()
                .getSecret();

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}