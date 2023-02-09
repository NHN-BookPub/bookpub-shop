package com.nhnacademy.bookpubshop.subscribe.service;

import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeProductRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 구독상품 관련 서비스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface SubscribeService {
    /**
     * 구독을 생성하기위한 메서드입니다.
     *
     * @param dto 구독생성 정보들 기
     */
    void createSubscribe(CreateSubscribeRequestDto dto, MultipartFile image);

    /**
     * 구독을 삭제하기위한 메서드입니다.
     *
     * @param subscribeNo 구독번호
     * @param used        사용여부
     */
    void deleteSubscribe(Long subscribeNo, boolean used);

    /**
     * 구독상품들이 반환됩니다.
     *
     * @param pageable 페이징 객체 기입
     * @return 구독정보들이 반환
     */
    Page<GetSubscribeResponseDto> getSubscribes(Pageable pageable);

    /**
     * 구독 정보를 수정할때 쓰이는 메서드입니다.
     *
     * @param dto         수정 정보가 들어갑니다.
     * @param subscribeNo 구독번호가 기입됩니다.
     * @param image 이미지 파일이 기입됩니다.
     */
    void modifySubscribe(ModifySubscribeRequestDto dto, Long subscribeNo, MultipartFile image);

    /**
     * 구독정보의 상세정보들을 출력합니다.
     *
     * @param subscribeNo 구독번호 기입.
     * @return 상세정보가 반환됩니다.
     */
    GetSubscribeDetailResponseDto getSubscribeDetail(Long subscribeNo);

    /**
     * 구독관련 연관상품을 추가하기위한 메서드입니다.
     *
     * @param subscribeNo 해당구독번호가 기입됩니다,.
     * @param dto  해당되는 상품번호들이 기입됩니다.
     */
    void addRelationProducts(Long subscribeNo, CreateSubscribeProductRequestDto dto);

    /**
     * 구독 갱신여부를 수정하기위한 메서드입니다.
     *
     * @param subscribeNo 구독 번호가 기입됩니다.
     * @param isRenewed   갱신여부가 기입됩니다.
     */
    void modifySubscribeRenewed(Long subscribeNo, boolean isRenewed);
}
