package com.nhnacademy.bookpubshop.reviewpolicy.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.CreateReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.request.ModifyPointReviewPolicyRequestDto;
import com.nhnacademy.bookpubshop.reviewpolicy.dto.response.GetReviewPolicyResponseDto;
import com.nhnacademy.bookpubshop.reviewpolicy.service.ReviewPolicyService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품평정책을 다루는 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
public class ReviewPolicyController {
    private final ReviewPolicyService reviewPolicyService;


    /**
     * 상품평 정책을 등록하기 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param createRequestDto 상품평 등록에 필요한 정보를 담은 Dto
     * @return the response entity
     */
    @AdminAuth
    @PostMapping("/token/review-policies")
    public ResponseEntity<Void> reviewPolicyAdd(@Valid @RequestBody CreateReviewPolicyRequestDto createRequestDto) {
        reviewPolicyService.createReviewPolicy(createRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품평 정책 지급 포인트를 수정하기 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param modifyRequestDto 상품평 수정에 필요한 정보를 담은 Dto
     * @return the response entity
     */
    @AdminAuth
    @PutMapping("/token/review-policies")
    public ResponseEntity<Void> reviewPolicyModify(@Valid @RequestBody ModifyPointReviewPolicyRequestDto modifyRequestDto) {
        reviewPolicyService.modifyReviewPolicy(modifyRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품평 정책 사용여부를 수정하기 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param policyNo 사용 상태로 설정하고 싶은 상품평 정책 번호
     * @return the response entity
     */
    @AdminAuth
    @PutMapping("/token/review-policies/{policyNo}/used")
    public ResponseEntity<Void> reviewPolicyModifyUsed(@PathVariable Integer policyNo) {
        reviewPolicyService.modifyUsedReviewPolicy(policyNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품평 정책 리스트 조회를 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/review-policies")
    public ResponseEntity<List<GetReviewPolicyResponseDto>> reviewPolicyList() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewPolicyService.getReviewPolicies());
    }
}
