package com.nhnacademy.bookpubshop.filemanager;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileUtils implements FileManagement {

    @Value("${file.save.path}")
    private String basePath;
    private final FileRepository fileRepository;

    /**
     * 파일을 저장하는 메소드입니다.
     *
     * @param file 파일
     */
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

        file.transferTo((Paths.get(basePath + nameSaved + fileExtension)));

        return fileRepository.save(new File(
                null,
                review,
                subscribe,
                inquiry,
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

    /**
     * 파일을 삭제해주는 메소드입니다.
     *
     * @param path 파일경로
     * @throws IOException the io exception
     */
    public void deleteFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        fileRepository.delete(fileRepository.findByFilePath(path));

        if (!resource.getFile().exists()) {
            throw new FileNotFoundException();
        }
        if (!resource.getFile().delete()) {
            throw new FileNotFoundException();
        }
    }


    /**
     * 파일을 로드해오는 메소드입니다.
     *
     * @param path 파일 경로
     * @return 바이트 형식의 이미지
     * @throws IOException the io exception
     */
    public String loadFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        return Base64.encodeBase64String(bytes);
    }

    @Override
    public GetDownloadInfo downloadFileInfo(String path) {
        return null;
    }
}
