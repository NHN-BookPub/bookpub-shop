package com.nhnacademy.bookpubshop.filemanager;

import com.nhnacademy.bookpubshop.config.ObjectStorageProperties;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.filemanager.dto.request.TokenRequest;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.filemanager.dto.response.TokenResponse;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * 오브젝트 스토리지 파일을 다루기 위한 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class ObjectStorageUtils implements FileManagement {

    private String tokenId;
    private LocalDateTime expires;

    private final ObjectStorageProperties properties;
    private static final String X_AUTH_TOKEN = "X-Auth-Token";
    private final RestTemplate restTemplate;
    private final FileRepository fileRepository;

    private String requestToken() {
        String identityUrl = properties.getIdentity() + "/tokens";

        if (Objects.isNull(tokenId)
                || expires.minusMinutes(1).isAfter(LocalDateTime.now())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            TokenRequest tokenRequest = new TokenRequest(properties.getTenantId(),
                    properties.getUsername(), properties.getPassword());

            HttpEntity<TokenRequest> httpEntity
                    = new HttpEntity<>(tokenRequest, headers);

            ResponseEntity<TokenResponse> response
                    = this.restTemplate.exchange(
                    identityUrl, HttpMethod.POST, httpEntity, TokenResponse.class);

            tokenId = Objects.requireNonNull(
                    response.getBody()).getAccess().getToken().getId();
            expires = Objects.requireNonNull(
                    response.getBody()).getAccess().getToken().getExpires();
        }

        return tokenId;
    }

    @Override
    public void deleteFile(String path) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        // API 호출
        this.restTemplate.exchange(path, HttpMethod.DELETE, requestHttpEntity, String.class);

        fileRepository.delete(fileRepository.findByFilePath(path));
    }


    @Override
    public File saveFile(Inquiry inquiry,
                         CouponTemplate couponTemplate,
                         Product product,
                         Review review,
                         CustomerService customerService,
                         Subscribe subscribe,
                         MultipartFile file,
                         String fileCategory,
                         String path) throws IOException {

        if (Objects.isNull(file)) {
            return null;
        }
        String originalFileName = file.getOriginalFilename();

        if (Objects.isNull(originalFileName)) {
            throw new NullPointerException();
        }

        int posImage = originalFileName.lastIndexOf(".");
        String nameOrigin = originalFileName.substring(0, posImage);
        String fileExtension = originalFileName.substring(posImage);
        String nameSaved = UUID.randomUUID().toString();

        String url = properties.getUrl() + "/" + properties.getContainerName() + "/"
                + path + "/" + nameSaved + fileExtension;

        InputStream inputStream = new ByteArrayInputStream(file.getBytes());

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = new RequestCallback() {
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add(X_AUTH_TOKEN, requestToken());
                IOUtils.copy(inputStream, request.getBody());
            }
        };

        // 오버라이드한 RequestCallback을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplateFactory = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<>(
                String.class, restTemplateFactory.getMessageConverters());

        // API 호출
        restTemplateFactory.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return fileRepository.save(new File(
                null,
                review,
                subscribe,
                inquiry,
                couponTemplate,
                product,
                customerService,
                fileCategory,
                url,
                fileExtension,
                nameOrigin,
                nameSaved
        ));
    }

    @Override
    public String loadFile(String path) {
        return path;
    }


    @Override
    public GetDownloadInfo downloadFileInfo(String path) {

        if (!fileRepository.existsByFilePath(path)) {
            throw new FileNotFoundException();
        }

        File file = fileRepository.findByFilePath(path);

        return new GetDownloadInfo(path, requestToken(),
                file.getNameOrigin(), file.getNameSaved(), file.getFileExtension());
    }
}
