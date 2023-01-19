package com.nhnacademy.bookpubshop.orderstatecode.service.impl;

import com.nhnacademy.bookpubshop.orderstatecode.dto.CreateOrderStateCodeRequestDto;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.orderstatecode.service.OrderStateCodeService;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문상태코드 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class OrderStateCodeServiceImpl implements OrderStateCodeService {
    private final OrderStateCodeRepository orderStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createPricePolicy(CreateOrderStateCodeRequestDto request) {
        orderStateCodeRepository.save(
                new OrderStateCode(null, request.getCodeName(),
                        request.isCodeUsed(),
                        request.getCodeInfo()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GetOrderStateCodeResponseDto getOrderStateCodeById(Integer pricePolicyNo) {
        return orderStateCodeRepository.findStateCodeByNo(pricePolicyNo)
                        .orElseThrow(NotFoundStateCodesException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetOrderStateCodeResponseDto> getOrderStateCodes() {
        return orderStateCodeRepository.findStateCodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyOrderStateCodeUsed(Integer codeNo) {
        OrderStateCode code = orderStateCodeRepository.findById(codeNo)
                .orElseThrow(NotFoundStateCodesException::new);

        code.modifyUsed();

        orderStateCodeRepository.save(code);
    }
}
