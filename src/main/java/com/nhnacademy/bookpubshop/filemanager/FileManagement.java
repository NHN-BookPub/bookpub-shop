package com.nhnacademy.bookpubshop.filemanager;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface FileManagement {

    /**
     * 파일을 저장하는 메서드입니다.
     *
     * @param personalInquiry the personal inquiry
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
    File saveFile(PersonalInquiry personalInquiry, CouponTemplate couponTemplate,
                  Product product, Review review, CustomerService customerService,
                  MultipartFile file, String fileCategory, String path) throws IOException;

    /**
     * 파일을 로드해오는 메서드입니다.
     *
     * @param path 로드해올 파일의 경로
     * @return 파일 내용
     * @throws IOException the io exception
     */
    String loadFile(String path) throws IOException;

    List<String> loadFiles(String path);

    String downloadFile(String path);

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
