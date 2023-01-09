package com.nhnacademy.bookpubshop.author.service.impl;

import com.nhnacademy.bookpubshop.author.dto.CreateAuthorRequestDto;
import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.author.service.AuthorService;
import java.util.ArrayList;
import java.util.List;
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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetAuthorResponseDto createAuthor(CreateAuthorRequestDto author) {
        Author created =
                authorRepository.save(new Author(null, author.getAuthorName()));

        return new GetAuthorResponseDto(created.getAuthorNo(), created.getAuthorName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable) {
        return authorRepository.getAuthorsByPage(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAuthorResponseDto> getAuthorsByName(String name) {
        List<Author> authors = authorRepository.getAuthorByAuthorName(name);

        List<GetAuthorResponseDto> returnAuthor = new ArrayList<>();

        for (Author author : authors) {
            returnAuthor.add(
                    new GetAuthorResponseDto(author.getAuthorNo(), author.getAuthorName()));
        }

        return returnAuthor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo) {
        List<GetAuthorResponseDto> response = authorRepository.getAuthorsByProductNo(productNo);

        if (response.isEmpty()) {
            throw new NotFoundAuthorException();
        }

        return response;
    }
}
