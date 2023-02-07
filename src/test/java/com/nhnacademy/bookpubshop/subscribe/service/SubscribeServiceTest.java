package com.nhnacademy.bookpubshop.subscribe.service;

import static com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepository;
import com.nhnacademy.bookpubshop.subscribe.service.impl.SubscribeServiceImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 구독 서비스 테스트
 *
 * @author : 유호철
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(SubscribeServiceImpl.class)
class SubscribeServiceTest {
    @Autowired
    SubscribeService service;

    @MockBean
    SubscribeRepository repository;
    @MockBean
    FileManagement fileManagement;

    Subscribe subscribe;
    CreateSubscribeRequestDto createSubscribeRequestDto;
    GetSubscribeResponseDto getSubscribeResponseDto;
    ModifySubscribeRequestDto modifySubscribeRequestDto;
    ArgumentCaptor<Subscribe> captor;
    File file;
    @BeforeEach
    void setUp() {
        subscribe = dummy();
        modifySubscribeRequestDto = modifyDummy();
        getSubscribeResponseDto = responseDummy();
        createSubscribeRequestDto = createDummy();
        captor = ArgumentCaptor.forClass(Subscribe.class);
        file = FileDummy.dummy(null, null, null,subscribe, null, null);
    }

    @DisplayName("구독생성 테스트 입니다.")
    @Test
    void createSubscribe() {
        when(repository.save(any()))
                .thenReturn(subscribe);

        service.createSubscribe(createSubscribeRequestDto, null);

        verify(repository, times(1))
                .save(captor.capture());
        Subscribe value = captor.getValue();
        assertThat(value.getSubscribeName()).isEqualTo(createSubscribeRequestDto.getName());
        assertThat(value.getSubscribePrice()).isEqualTo(createSubscribeRequestDto.getPrice());
        assertThat(value.getSalesPrice()).isEqualTo(createSubscribeRequestDto.getSalePrice());
        assertThat(value.getSalesRate()).isEqualTo(createSubscribeRequestDto.getSalesRate());
        assertThat(value.isSubscribeRenewed()).isEqualTo(createSubscribeRequestDto.isRenewed());

    }

    @DisplayName("구독을 찾을수 없을경우 발생하는 에러")
    @Test
    void deleteSubscribeFail() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteSubscribe(1L, true))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);

    }

    @DisplayName("구독삭제")
    @Test
    void deleteSubscribe() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));

        service.deleteSubscribe(1L, true);

        verify(repository, times(1))
                .findById(1L);
    }

    @DisplayName("구독정보들을 반환하기위한 메서드입니다.")
    @Test
    void getSubscribes() {
        PageImpl<GetSubscribeResponseDto> result = new PageImpl<>(List.of(getSubscribeResponseDto));
        PageRequest page = PageRequest.of(0, 10);
        when(repository.getSubscribes(any(Pageable.class)))
                .thenReturn(result);

        Page<GetSubscribeResponseDto> response = service.getSubscribes(page);

        assertThat(response.getContent().get(0).getSubscribeNo()).isEqualTo(getSubscribeResponseDto.getSubscribeNo());
        assertThat(response.getContent().get(0).getSubscribeName()).isEqualTo(getSubscribeResponseDto.getSubscribeName());
        assertThat(response.getContent().get(0).getPrice()).isEqualTo(getSubscribeResponseDto.getPrice());
        assertThat(response.getContent().get(0).getSalePrice()).isEqualTo(getSubscribeResponseDto.getSalePrice());
        assertThat(response.getContent().get(0).getSalesRate()).isEqualTo(getSubscribeResponseDto.getSalesRate());
        assertThat(response.getContent().get(0).getViewCnt()).isEqualTo(getSubscribeResponseDto.getViewCnt());
        assertThat(response.getContent().get(0).isDeleted()).isEqualTo(getSubscribeResponseDto.isDeleted());
        assertThat(response.getContent().get(0).isRenewed()).isEqualTo(getSubscribeResponseDto.isRenewed());

        verify(repository, times(1))
                .getSubscribes(page);
    }

    @DisplayName("구독 정보를 찾지 못할경우 Test")
    @Test
    void modifySubscribe() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.modifySubscribe(modifySubscribeRequestDto, 1L, any()))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);

    }

    @DisplayName("구독 정보 수정 성공 테스트")
    @Test
    void modifySubscribeSuccess() {
        ReflectionTestUtils.setField(subscribe, "file", file);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));
        service.modifySubscribe(modifySubscribeRequestDto, 1L, any());

        verify(repository, times(1))
                .findById(1L);
    }
}