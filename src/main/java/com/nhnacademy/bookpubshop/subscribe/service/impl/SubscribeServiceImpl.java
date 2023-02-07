package com.nhnacademy.bookpubshop.subscribe.service.impl;

import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.FileException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepository;
import com.nhnacademy.bookpubshop.subscribe.service.SubscribeService;
import java.io.IOException;
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
    private final FileManagement fileManagement;
    public static final String SUBSCRIBE = "subscribe";

    /**
     * {@inheritDoc}
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
    public void modifySubscribe(ModifySubscribeRequestDto dto, Long subscribeNo,
                                MultipartFile image) {
        Subscribe subscribe = subscribeRepository.findById(subscribeNo)
                .orElseThrow(SubscribeNotFoundException::new);
        File file;
        try {
            fileManagement.deleteFile(subscribe.getFile().getFilePath());
            file = fileManagement.saveFile(null, null, null,
                    null, null, subscribe, image, SUBSCRIBE, SUBSCRIBE);
        } catch (IOException e) {
            throw new FileException();
        }
        subscribe.modifySubscribeInfo(dto.getName(),
                dto.getSalePrice(),
                dto.getPrice(),
                dto.getSaleRate(),
                dto.isRenewed(),
                dto.isDeleted());
        subscribe.setFile(file);

    }
}
