package com.nhnacademy.bookpubshop.utils;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일을 읽어 처리하는 Util 클래스.
 *
 * @author : 정유진
 * @since : 1.0
 **/

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUtils {

    @Value("${file.save.path}")
    private String basePath;
    private final FileRepository fileRepository;

    /**
     * 파일을 저장하는 메소드입니다.
     *
     * @param file 파일
     */
    public File saveFile(PersonalInquiry personalInquiry,
                         CouponTemplate couponTemplate,
                         Product product,
                         Review review,
                         CustomerService customerService,
                         MultipartFile file,
                         String fileCategory) throws IOException {

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

        file.transferTo((Paths.get(basePath + nameSaved + fileExtension)));

        return fileRepository.save(new File(
                null,
                review,
                personalInquiry,
                couponTemplate,
                product,
                customerService,
                fileCategory,
                "static/image/" + nameSaved + fileExtension,
                fileExtension,
                nameOrigin,
                nameSaved
        ));
    }

    public void deleteFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        fileRepository.delete(fileRepository.findByFilePath(path));

        if (resource.getFile().exists()) {
            resource.getFile().delete();
        }
    }


    public String loadFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        log.info("*******************" + resource.getPath());

        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        return Base64.encodeBase64String(bytes);
    }
}
