package com.nhnacademy.bookpubshop.file.dummy;

import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;

/**
 * File 개체의 더미입니다.
 *
 * @author : 유호철, 여운석
 * @since : 1.0
 **/
public class FileDummy {

    public static File dummy(Inquiry inquiry,
                             Review review,
                             CouponTemplate couponTemplate,
                             Product product,
                             CustomerService customerService,
                             Subscribe subscribe) {
        return new File(
                null, review,
                subscribe,
                inquiry,
                couponTemplate, product,
                customerService,
                "file_category_test",
                "test_path",
                "file_ex",
                "file_name",
                "saved"
        );
    }

    public static File dummy(Inquiry inquiry,
                             Review review,
                             CouponTemplate couponTemplate,
                             Product product,
                             CustomerService customerService,
                             FileCategory category,
                             Subscribe subscribe) {
        return new File(null, review,
                subscribe,
                inquiry,
                couponTemplate, product,
                customerService,
                category.getCategory(),
                "test_path",
                "file_ex",
                "file_name",
                "saved");
    }
}
