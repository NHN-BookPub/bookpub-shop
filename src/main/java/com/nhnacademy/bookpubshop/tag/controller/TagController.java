package com.nhnacademy.bookpubshop.tag.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.tag.dto.request.AddTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.request.ModifyTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.service.TagService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Tag Api RestController 입니다.
 *
 * @author : 박경서
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * 단건 태그를 조회하는 메서드입니다.
     *
     * @param tagNo 태그를 조회하기 위한 태그번호
     * @return 성공 경우 200, 태그 정보 응답
     */
    @GetMapping("/api/tags/{tagNo}")
    public ResponseEntity<GetTagResponseDto> tagDetails(
            @PathVariable(name = "tagNo") Integer tagNo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tagService.getTag(tagNo));
    }

    /**
     * 태그 전체 리스트를 조회하는 메서드입니다.
     *
     * @return 성공 경우 200, 태그 리스트 응답
     */
    @GetMapping("/api/tags")
    public ResponseEntity<List<GetTagResponseDto>> tagList() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tagService.getTags());
    }

    /**
     * 태그 등록을 하는 메서드입니다.
     *
     * @param request 태그 생성을 위한 태그 정보
     * @return 성공 경우 201 응답
     */
    @PostMapping("/token/tags")
    @AdminAuth
    public ResponseEntity<AddTagRequestDto> tagAdd(@Valid @RequestBody AddTagRequestDto request) {
        tagService.addTag(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request);
    }

    /**
     * 태그 수정을 하는 메서드입니다.
     *
     * @param request 태그 수정을 위한 태그 정보
     * @return 성공 경우 201 응답
     */
    @PutMapping("/token/tags")
    @AdminAuth
    public ResponseEntity<ModifyTagRequestDto> tagModify(
            @Valid @RequestBody ModifyTagRequestDto request) {
        tagService.modifyTagInformation(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request);
    }

    /**
     * 태그 삭제를 위한 메서드입니다.
     *
     * @param tagNo 태그 삭제를 위한 태그 번호
     * @return 성공 경우 200 응답
     */
    @DeleteMapping("/token/tags/{tagNo}")
    @AdminAuth
    public ResponseEntity<Void> tagDelete(@PathVariable("tagNo") Integer tagNo) {
        tagService.deleteTagByTagNumber(tagNo);

        return ResponseEntity.ok()
                .build();
    }

}
