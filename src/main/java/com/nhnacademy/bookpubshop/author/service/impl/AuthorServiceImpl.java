package com.nhnacademy.bookpubshop.author.service.impl;

import com.nhnacademy.bookpubshop.author.dto.request.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.request.ModifyAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.author.service.AuthorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 저자 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createAuthor(CreateAuthorRequestDto author) {
        authorRepository.save(new Author(null, author.getAuthorName(), author.getMainBook()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyAuthor(Integer authorNo, ModifyAuthorRequestDto dto) {
        Author author = authorRepository.findById(authorNo)
                .orElseThrow(NotFoundAuthorException::new);

        author.modifyAuthorInfo(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable) {
        return authorRepository.getAuthorsByPage(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetAuthorResponseDto> getAuthorsByName(String name) {
        List<GetAuthorResponseDto> responses = authorRepository.getAuthorByName(name);

        if (responses.isEmpty()) {
            throw new NotFoundAuthorException();
        }

        return authorRepository.getAuthorByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo) {
        List<GetAuthorResponseDto> response = authorRepository.getAuthorsByProductNo(productNo);

        if (response.isEmpty()) {
            throw new NotFoundAuthorException();
        }

        return response;
    }
}
