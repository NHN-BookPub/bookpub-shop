package com.nhnacademy.bookpubshop.error;

import com.nhnacademy.bookpubshop.address.exception.AddressNotFoundException;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.card.exception.NotSupportedCompanyException;
import com.nhnacademy.bookpubshop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.coupon.exception.CouponNotFoundException;
import com.nhnacademy.bookpubshop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.bookpubshop.couponmonth.exception.CouponMonthNotFoundException;
import com.nhnacademy.bookpubshop.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookpubshop.couponstatecode.exception.CouponStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontype.exception.CouponTypeNotFoundException;
import com.nhnacademy.bookpubshop.error.dto.response.ErrorResponse;
import com.nhnacademy.bookpubshop.member.exception.AuthorityNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.IdAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.NicknameAlreadyExistsException;
import com.nhnacademy.bookpubshop.order.exception.NotFoundOrderStateNameException;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.orderstatecode.exception.NotFoundOrderStateException;
import com.nhnacademy.bookpubshop.paymentstatecode.exception.NotFoundPaymentStateException;
import com.nhnacademy.bookpubshop.paymenttypestatecode.exception.NotFoundPaymentTypeException;
import com.nhnacademy.bookpubshop.personalinquiry.exception.PersonalInquiryNotFoundException;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.purchase.exception.NotFoundPurchasesException;
import com.nhnacademy.bookpubshop.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookpubshop.reviewpolicy.exception.ReviewPolicyNotFoundException;
import com.nhnacademy.bookpubshop.reviewpolicy.exception.ReviewPolicyUsedNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.tag.exception.TagNameDuplicatedException;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tier.exception.TierAlreadyExists;
import com.nhnacademy.bookpubshop.wishlist.exception.WishlistNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역적으로 예외를 잡기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 */
@Log4j2
@RestControllerAdvice
public class ShopAdviceController {

    /**
     * 벨리데이션 오류를 잡기위한 메서드입니다.
     *
     * @param exception 벨리데이션 오류시 발생하는 에러가 받아서 들어옵니다.
     * @return response entity 에러메세지들이 400 메시지를 받고 반환.
     * @author : 유호철
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionResponse>> validationException(
            MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException exception : {}", exception.getMessage());

        List<ExceptionResponse> exceptions = exception.getBindingResult().getAllErrors().stream()
                .map(defaultMessage -> new ExceptionResponse(defaultMessage.getDefaultMessage()))
                .collect(Collectors.toList());
        log.warn("Error value exceptions : {}", exceptions);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptions);
    }

    @ExceptionHandler(value =
            {
                AddressNotFoundException.class, NotFoundAuthorException.class,
                NotSupportedCompanyException.class, CategoryNotFoundException.class,
                CouponNotFoundException.class, NotFoundCouponException.class,
                CouponMonthNotFoundException.class, CouponTemplateNotFoundException.class,
                MemberNotFoundException.class, NicknameAlreadyExistsException.class,
                PersonalInquiryNotFoundException.class, ProductNotFoundException.class,
                ReviewNotFoundException.class, SubscribeNotFoundException.class,
                TagNotFoundException.class, OrderNotFoundException.class,

            })
    public ResponseEntity<ErrorResponse> errorhandler() {
        log.warn("MAIN Bad Request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse("MAIN", "MAIN"));
    }

    @ExceptionHandler(value =
            {
                IdAlreadyExistsException.class,
                NotFoundOrderStateException.class, NotFoundPaymentStateException.class,
                NotFoundPaymentTypeException.class, NotFoundProductPolicyException.class,
                NotFoundPricePolicyException.class, NotFoundStateCodeException.class,
                NotFoundStateCodesException.class, CouponTypeNotFoundException.class,
                CouponStateCodeNotFoundException.class, CouponPolicyNotFoundException.class,
                ReviewPolicyNotFoundException.class, ReviewPolicyUsedNotFoundException.class,
                WishlistNotFoundException.class
            })
    public ResponseEntity<Void> badRequest() {
        log.warn("only BadRequest");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(value = NotFoundOrderStateNameException.class)
    public ResponseEntity<ErrorResponse> orderStateNameError() {
        log.warn("not defined order type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(NotFoundOrderStateNameException.MESSAGE, "ORDER"));
    }

    @ExceptionHandler(value = CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> categoryError() {
        log.warn("category page badRequest");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(CategoryAlreadyExistsException.MESSAGE, "CATEGORY"));
    }

    @ExceptionHandler(value = AuthorityNotFoundException.class)
    public ResponseEntity<ErrorResponse> authority() {
        log.warn("authority badrequest");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(AddressNotFoundException.MESSAGE, "AUTHORITY"));
    }

    @ExceptionHandler(value = NotFoundPurchasesException.class)
    public ResponseEntity<ErrorResponse> adminException() {
        log.warn("Admin Page bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(NotFoundPurchasesException.MESSAGE, "ADMIN"));
    }

    @ExceptionHandler(value = TagNameDuplicatedException.class)
    public ResponseEntity<ErrorResponse> tagError() {
        log.warn("Tag bad Request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(TagNameDuplicatedException.ERROR_MESSAGE, "TAG"));
    }

    @ExceptionHandler(value = TierAlreadyExists.class)
    public ResponseEntity<ErrorResponse> tierError() {
        log.warn("tier bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(TierAlreadyExists.MESSAGE, "TIER"));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Void> error() {
        log.error("알수없는 에러");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
