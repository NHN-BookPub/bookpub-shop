package com.nhnacademy.bookpubshop.tag.service.impl;

import com.nhnacademy.bookpubshop.tag.dto.request.AddTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.request.ModifyTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.exception.TagNameDuplicatedException;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import com.nhnacademy.bookpubshop.tag.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagService 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * {@inheritDoc}
     *
     * @throws TagNotFoundException 태그가 없는 경우 발생하는 에러
     */
    @Override
    public GetTagResponseDto getTag(Integer tagNo) {
        return tagRepository.findTag(tagNo).orElseThrow(() -> new TagNotFoundException(tagNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetTagResponseDto> getTags() {
        return tagRepository.findTags();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addTag(AddTagRequestDto request) {
        if (tagRepository.existsByTagName(request.getAddTagName())) {
            throw new TagNameDuplicatedException(request.getAddTagName());
        }

        Tag tag = new Tag(null, request.getAddTagName(), request.getAddColorCode());
        tagRepository.save(tag);
    }

    /**
     * {@inheritDoc}
     *
     * @throws TagNotFoundException       태그 번호를 찾지 못한 경우 발생하는 에러
     * @throws TagNameDuplicatedException 태그이름이 중복된 경우 발생하는 에러
     */
    @Override
    @Transactional
    public void modifyTagInformation(ModifyTagRequestDto request) {
        Tag tag = tagRepository.findById(request.getModifyTagNo())
                .orElseThrow(() -> new TagNotFoundException(request.getModifyTagNo()));

        if (tagRepository.existsByTagName(request.getModifyTagName())) {
            throw new TagNameDuplicatedException(request.getModifyTagName());
        }

        tag.modifyTagInfo(request.getModifyTagName(), request.getModifyColorCode());
    }

    /**
     * {@inheritDoc}
     *
     * @throws TagNotFoundException 태그를 찾지 못하는 경우 발생하는 에러
     */
    @Override
    @Transactional
    public void deleteTagByTagNumber(Integer tagNo) {
        Tag tag = tagRepository.findById(tagNo).orElseThrow(() -> new TagNotFoundException(tagNo));

        tagRepository.delete(tag);
    }
}
