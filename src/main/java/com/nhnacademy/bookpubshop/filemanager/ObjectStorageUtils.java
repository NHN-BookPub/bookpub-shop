package com.nhnacademy.bookpubshop.filemanager;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.filemanager.dto.request.TokenRequest;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.filemanager.dto.response.TokenResponse;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
    private static final String STORAGE_URL = "https://api-storage.cloud.toast.com/v1/AUTH_fcb81f74e379456b8ca0e091d351a7af";
    private static final String CONTAINER_NAME = "bookpub";

    private static final String TENANTID = "fcb81f74e379456b8ca0e091d351a7af";

    private static final String USERNAME = "hjk3530@naver.com";

    private static final String PASSWORD = "bookpub";

    private static final String IDENTITY = "https://api-identity.infrastructure.cloud.toast.com/v2.0";

    private static final String X_AUTH_TOKEN = "X-Auth-Token";
    private final RestTemplate restTemplate = new RestTemplate();
    private final FileRepository fileRepository;

    private String requestToken() {
        String identityUrl = IDENTITY + "/tokens";

        if (Objects.isNull(tokenId)
                || expires.minusMinutes(1).isAfter(LocalDateTime.now())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            TokenRequest tokenRequest = new TokenRequest(TENANTID, USERNAME, PASSWORD);

            HttpEntity<TokenRequest> httpEntity
                    = new HttpEntity<>(tokenRequest, headers);

            ResponseEntity<TokenResponse> response
                    = this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, TokenResponse.class);

            tokenId = Objects.requireNonNull(response.getBody()).getAccess().getToken().getId();
            expires = Objects.requireNonNull(response.getBody()).getAccess().getToken().getExpires();
        }

        return tokenId;
    }


    @Override
    public List<String> loadFiles(String path) {
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        // API 호출
        ResponseEntity<String> response
                = this.restTemplate.exchange(path, HttpMethod.GET, requestHttpEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // String으로 받은 목록을 배열로 변환
            return Arrays.asList(Objects.requireNonNull(response.getBody()).split("\\r?\\n"));
        }

        return Collections.emptyList();
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


    public File saveFile(PersonalInquiry personalInquiry,
                         CouponTemplate couponTemplate,
                         Product product,
                         Review review,
                         CustomerService customerService,
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

        String url = STORAGE_URL + "/" + CONTAINER_NAME + "/" + path + "/" + nameSaved + fileExtension;

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
                = new HttpMessageConverterExtractor<>(String.class, restTemplateFactory.getMessageConverters());

        // API 호출
        restTemplateFactory.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return fileRepository.save(new File(
                null,
                review,
                personalInquiry,
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
        String url = path + "?limit=1";
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        headers.setAccept(List.of(MediaType.MULTIPART_FORM_DATA));

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        // API 호출
        ResponseEntity<String> response
                = this.restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, String.class);

        if (response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()) {
            throw new FileNotFoundException();
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            // String으로 받은 목록을 배열로 변환
            List<String> list = Arrays.asList(Objects.requireNonNull(response.getBody()).split("\\r?\\n"));
            return list.get(0);
        }
        return null;
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
