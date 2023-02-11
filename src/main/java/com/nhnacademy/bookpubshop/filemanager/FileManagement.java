package com.nhnacademy.bookpubshop.filemanager;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일을 다루기 위한 인터페이스입니다.
 * FileUtils 구현체는 로컬 저장소,
 * ObjectStorageUtils 구현체는 오브젝트 스토리지를 사용합니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface FileManagement {

    /**
     * 파일을 저장하는 메서드입니다.
     *
     * @param inquiry         the inquiry
     * @param couponTemplate  the coupon template
     * @param product         the product
     * @param review          the review
     * @param customerService the customer service
     * @param file            the file
     * @param fileCategory    the file category
     * @param path            the path
     * @return the file
     * @throws IOException the io exception
     */
    File saveFile(Inquiry inquiry,
                  CouponTemplate couponTemplate,
                  Product product, Review review,
                  CustomerService customerService,
                  Subscribe subscribe,
                  MultipartFile file, String fileCategory, String path) throws IOException;

    /**
     * 파일을 로드해오는 메서드입니다.
     *
     * @param path 로드해올 파일의 경로
     * @return 파일 내용
     * @throws IOException the io exception
     */
    String loadFile(String path) throws IOException;

    /**
     * 파일 다운로드를 위한 정보를 반환하는 메서드입니다.
     *
     * @param path 파일 경로
     * @return 파일 다운로드를 위한 정보
     */
    GetDownloadInfo downloadFileInfo(String path);

    /**
     * 파일을 삭제하는 메서드입니다.
     *
     * @param path 삭제할 파일 경로
     * @throws IOException the io exception
     */
    void deleteFile(String path) throws IOException;
}
