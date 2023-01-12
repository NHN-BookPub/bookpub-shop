package com.nhnacademy.bookpubshop.file.dummy;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.customer_service.entity.CustomerService;
import com.nhnacademy.bookpubshop.review.entity.Review;
import java.time.LocalDateTime;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class FileDummy {

    public static File dummy(PersonalInquiry personalInquiry,
                             Review review,
                             CouponTemplate couponTemplate,
                             Product product,
                             CustomerService customerService) {
        return new File(
                null,review, personalInquiry,
                couponTemplate, product,
                customerService,
                "file_category_test",
                "test_path",
                "file_ex",
                "file_name",
                "saved"
        );
    }
}
