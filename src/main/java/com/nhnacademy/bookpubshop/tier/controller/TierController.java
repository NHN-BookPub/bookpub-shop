package com.nhnacademy.bookpubshop.tier.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.tier.dto.request.CreateTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.ModifyTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.service.TierService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 등급에 관한 Controller 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TierController {
    private final TierService tierService;

    /**
     * 등급을 등록을 요청할때 쓰이는 메서드입니다.
     *
     * @param createTierRequestDto 등급을 생성하기위한 값기입.
     * @return 성공했을시 응답코드 CREATED 201이 반환된다.
     */
    @PostMapping("/token/tiers")
    @AdminAuth
    public ResponseEntity<Void> tierAdd(
            @Valid @RequestBody CreateTierRequestDto createTierRequestDto) {
        tierService.addTier(createTierRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 등급을 수정을 요청할때 쓰이는 메서드입니다.
     *
     * @param modifyTierRequestDto 등급을 수정하기위한 값 기입.
     * @return 성공했을시 응답코드 CREATED 201이 반환된다.
     */
    @PutMapping("/token/tiers")
    @AdminAuth
    public ResponseEntity<Void> tierModify(
            @Valid @RequestBody ModifyTierRequestDto modifyTierRequestDto) {
        tierService.modifyTier(modifyTierRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 등급을 단일값을 조회 요청을 할때 쓰이는 메서드 입니다.
     *
     * @param tierNo 등급을 값을 조회하기위한 값.
     * @return 성공했을시 응답코드 OK 200이 반환된다.
     */
    @GetMapping("/api/tiers/{tierNo}")
    public ResponseEntity<TierResponseDto> tierDetails(@PathVariable("tierNo") Integer tierNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tierService.getTier(tierNo));
    }

    /**
     * 등급의 사용중인 이름을 확인하기위한 메서드입니다.
     *
     * @param name 등급 이름
     * @return boolean 값이 반환됩니다.
     */
    @GetMapping("/token/tiers/check-tierName")
    @AdminAuth
    public ResponseEntity<Boolean> tierNameCheck(@RequestParam("tierName") String name){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tierService.getTierName(name));
    }

    /**
     * 등급을 전체값을 조회 요청을 할때 쓰이는 메서드 입니다.
     *
     * @return 성공했을시 응답코드 OK 200이 반환된다.
     */
    @GetMapping("/token/tiers")
    @AdminAuth
    public ResponseEntity<List<TierResponseDto>> tierList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tierService.getTiers());
    }
}
