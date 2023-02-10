package com.nhnacademy.bookpubshop.subscribe.service.impl;

import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.FileException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeProductRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.SubscribeProductList;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepository;
import com.nhnacademy.bookpubshop.subscribe.service.SubscribeService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ProductRepository productRepository;
    private final FileManagement fileManagement;
    public static final String SUBSCRIBE = "subscribe";

    /**
     * {@inheritDoc}
     *
     * @throws FileException 파일에서 나는 IO Exception.
     */
    @Transactional
    @Override
    public void createSubscribe(CreateSubscribeRequestDto dto, MultipartFile image) {
        Subscribe subscribe = dto.dtoToEntity();
        File file;
        try {
            file = fileManagement.saveFile(null, null, null,
                    null, null, subscribe, image, SUBSCRIBE, SUBSCRIBE);
        } catch (IOException e) {
            throw new FileException();
        }
        subscribe.setFile(file);
        subscribeRepository.save(subscribe);
    }

    /**
     * {@inheritDoc}
     *
     * @throws SubscribeNotFoundException 구독이 없을경우발생.
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
     *
     * @throws SubscribeNotFoundException 구독이없을경우발생.
     * @throws FileException              파일 에서 나는 IO Exception.
     */
    @Transactional
    @Override
    public void modifySubscribe(ModifySubscribeRequestDto dto, Long subscribeNo,
                                MultipartFile image) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
        File file;
        try {
            file = fileManagement.saveFile(null, null, null,
                    null, null, subscribe, image, SUBSCRIBE, SUBSCRIBE);
        } catch (IOException e) {
            throw new FileException();
        }
        subscribe.modifySubscribeInfo(dto.getName(),
                dto.getSalePrice(),
                dto.getPrice(),
                dto.getSaleRate(),
                dto.isRenewed());
        subscribe.setFile(file);
    }

    /**
     * {@inheritDoc}
     *
     * @throws SubscribeNotFoundException 구독이없을경우 발생.
     */
    @Override
    public GetSubscribeDetailResponseDto getSubscribeDetail(Long subscribeNo) {
        return subscribeRepository.getSubscribeDetail(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     *
     * @throws SubscribeNotFoundException 구독이 없을경우 발생.
     */
    @Transactional
    @Override
    public void addRelationProducts(Long subscribeNo,
                                    CreateSubscribeProductRequestDto productNos) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
        addRelationProduct(productNos.getProductNo(), subscribe);
    }

    /**
     * {@inheritDoc}
     *
     * @throws SubscribeNotFoundException 구독이없을경우 발생.
     */
    @Transactional
    @Override
    public void modifySubscribeRenewed(Long subscribeNo, boolean isRenewed) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
        subscribe.changeIsRenewed(isRenewed);
    }

    /**
     * 구독에 해당하는 연관상품들을 추가하기위한 메서드입니다.
     *
     * @param productNos 상품번호들이 들어옵니다.
     * @param subscribe  구독정보가 들어옵니다.
     * @throws ProductNotFoundException 상품이없을경우발생.
     */
    private void addRelationProduct(List<Long> productNos,
                                    Subscribe subscribe) {
        List<SubscribeProductList> list = productNos.stream()
                .map(productNo -> productRepository.findById(productNo)
                        .orElseThrow(ProductNotFoundException::new))
                .map(p -> new SubscribeProductList(null, subscribe, p))
                .collect(Collectors.toList());
        subscribe.removeRelationList();

        list.forEach(subscribe::addRelationList);
    }
}
