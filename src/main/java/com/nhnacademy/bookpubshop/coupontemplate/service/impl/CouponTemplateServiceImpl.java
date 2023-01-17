package com.nhnacademy.bookpubshop.coupontemplate.service.impl;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.couponstatecode.exception.CouponStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.couponstatecode.repository.CouponStateCodeRepository;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.coupontype.exception.CouponTypeNotFoundException;
import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.utils.FileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * CouponTemplate 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponTemplateServiceImpl implements CouponTemplateService {
    private final CouponTemplateRepository couponTemplateRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTypeRepository couponTypeRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CouponStateCodeRepository couponStateCodeRepository;
    private final FileUtils fileUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public RestGetDetailCouponTemplateResponseDto getDetailCouponTemplate(Long templateNo) throws IOException {
        GetDetailCouponTemplateResponseDto detailDto =
                couponTemplateRepository.findDetailByTemplateNo(templateNo)
                        .orElseThrow(() -> new CouponTemplateNotFoundException(templateNo));

        return detailDto.transform(fileUtils.loadFile(detailDto.getTemplateImage()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetDetailCouponTemplateResponseDto> getDetailCouponTemplates(Pageable pageable) {
        return couponTemplateRepository.findDetailAllBy(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<RestGetCouponTemplateResponseDto> getCouponTemplates(Pageable pageable) throws IOException {
        Page<GetCouponTemplateResponseDto> dto = couponTemplateRepository.findAllBy(pageable);

        List<GetCouponTemplateResponseDto> dtolist = dto.getContent();
        List<RestGetCouponTemplateResponseDto> restList = new ArrayList<>();

        for (GetCouponTemplateResponseDto tmpDto : dtolist) {
            if (Objects.nonNull(tmpDto.getTemplateImage())) {
                restList.add(tmpDto.transform(
                        fileUtils.loadFile(tmpDto.getTemplateImage()
                        )));
            }
        }

        return new PageImpl<>(restList, pageable, dto.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createCouponTemplate(CreateCouponTemplateRequestDto createRequestDto,
                                     MultipartFile image) throws IOException {

        CouponTemplate couponTemplate = createRequestDto.createCouponTemplate(
                getCouponPolicy(createRequestDto.getPolicyNo()),
                getCouponType(createRequestDto.getTypeNo()),
                getProduct(createRequestDto.getProductNo()),
                getCategory(createRequestDto.getCategoryNo()),
                getCouponStateCode(createRequestDto.getCodeNo())
        );

        couponTemplateRepository.save(couponTemplate);

        if (Objects.nonNull(image)) {
            fileUtils.saveFile(null, couponTemplate, null, null, null, image, "coupon");
        }
    }

    /**
     * {@inheritDoc}
     */
    // 이미지 첨부 이슈 해결 후 수정 해야함.
    @Override
    @Transactional
    public void modifyCouponTemplate(Long templateNo, ModifyCouponTemplateRequestDto modifyRequestDto) {

        if (!couponTemplateRepository.existsById(templateNo)) {
            throw new CouponTemplateNotFoundException(templateNo);
        }

        couponTemplateRepository.save(new CouponTemplate(
                templateNo,
                getCouponPolicy(modifyRequestDto.getPolicyNo()),
                getCouponType(modifyRequestDto.getTypeNo()),
                getProduct(modifyRequestDto.getProductNo()),
                getCategory(modifyRequestDto.getCategoryNo()),
                getCouponStateCode(modifyRequestDto.getCodeNo()),
                modifyRequestDto.getTemplateName(),
                modifyRequestDto.getFinishedAt(),
                modifyRequestDto.getIssuedAt(),
                modifyRequestDto.isTemplateOverlapped(),
                modifyRequestDto.isTemplateBundled()
        ));
    }

    /**
     * 상품번호로 상품을 가져오기 위한 메소드.
     *
     * @param productNo 조회할 상품 번호
     * @return Product 상품 entity
     */
    private Product getProduct(Long productNo) {
        if (Objects.isNull(productNo)) {
            return null;
        }
        return productRepository.findById(productNo)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * 카테고리번호로 카테고리를 가져오기 위한 메소드.
     *
     * @param categoryNo 조회할 카테고리 번호
     * @return Category 카테고리 entity
     */
    private Category getCategory(Integer categoryNo) {
        if (Objects.isNull(categoryNo)) {
            return null;
        }
        return categoryRepository.findById(categoryNo)
                .orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * 쿠폰유형번호로 쿠폰유형을 가져오기 위한 메소드.
     *
     * @param typeNo 조회할 쿠폰유형 번호
     * @return CouponType 쿠폰유형 entity
     */
    private CouponType getCouponType(Long typeNo) {
        return couponTypeRepository.findById(typeNo)
                .orElseThrow(() -> new CouponTypeNotFoundException(typeNo));
    }

    /**
     * 쿠폰정책번호 쿠폰정책을 가져오기 위한 메소드.
     *
     * @param policyNo 조회할 쿠폰정책 번호
     * @return CouponPolicy 쿠폰정책 entity
     */
    private CouponPolicy getCouponPolicy(Integer policyNo) {
        return couponPolicyRepository.findById(policyNo)
                .orElseThrow(() -> new CouponPolicyNotFoundException(policyNo));
    }

    /**
     * 쿠폰상태코드번호로 쿠폰상태코드를 가져오기 위한 메소드.
     *
     * @param codeNo 조회할 쿠폰상태코드 번호
     * @return CouponStateCode 쿠폰상태코드 entity
     */
    private CouponStateCode getCouponStateCode(Integer codeNo) {
        return couponStateCodeRepository.findById(codeNo)
                .orElseThrow(() -> new CouponStateCodeNotFoundException(codeNo));
    }
}
