package com.nhnacademy.bookpubshop.order.relationship.service.impl;

import com.nhnacademy.bookpubshop.order.relationship.dto.CreateOrderProductStateCodeRequestDto;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductStateCodeService;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문상품상태코드 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class OrderProductStateCodeServiceImpl implements OrderProductStateCodeService {
    private final OrderProductStateCodeRepository orderProductStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createOrderProductStateCode(CreateOrderProductStateCodeRequestDto request) {
        orderProductStateCodeRepository.save(new OrderProductStateCode(
                null, request.getCodeName(), request.isCodeUsed(), request.getCodeInfo()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyUsedOrderProductStateCode(Integer codeNo, boolean used) {
        OrderProductStateCode code = orderProductStateCodeRepository.findById(codeNo)
                .orElseThrow(NotFoundStateCodeException::new);
        code.modifyUsed(used);
        orderProductStateCodeRepository.save(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetOrderProductStateCodeResponseDto getOrderProductStateCode(Integer codeNo) {
        return orderProductStateCodeRepository.findCodeById(codeNo)
                .orElseThrow(NotFoundStateCodeException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetOrderProductStateCodeResponseDto> getOrderProductStateCodes() {
        return orderProductStateCodeRepository.findCodeAll();
    }
}
