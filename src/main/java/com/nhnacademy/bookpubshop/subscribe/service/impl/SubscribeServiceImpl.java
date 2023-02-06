package com.nhnacademy.bookpubshop.subscribe.service.impl;

import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepository;
import com.nhnacademy.bookpubshop.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구독상품관련 구현클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void createSubscribe(CreateSubscribeRequestDto dto) {
        subscribeRepository.save(dto.dtoToEntity());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteSubscribe(Long subscribeNo, boolean used) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
        subscribe.changeIsDeleted(used);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSubscribeResponseDto> getSubscribes(Pageable pageable) {
        return subscribeRepository.getSubscribes(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifySubscribe(ModifySubscribeRequestDto dto, Long subscribeNo) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);

        subscribe.modifySubscribeInfo(dto.getName(),
                dto.getSalePrice(),
                dto.getPrice(),
                dto.getSaleRate(),
                dto.isRenewed(),
                dto.isDeleted());
    }
}
