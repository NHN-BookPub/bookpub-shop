package com.nhnacademy.bookpubshop.author.repository.impl;

import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.entity.QAuthor;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepositoryCustom;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductAuthor;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 저자 Custom repository의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public class AuthorRepositoryImpl extends QuerydslRepositorySupport
        implements AuthorRepositoryCustom {
    /**
     * 생성자입니다.
     */
    public AuthorRepositoryImpl() {
        super(Author.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable) {
        QAuthor author = QAuthor.author;

        return from(author)
                .select(Projections.constructor(GetAuthorResponseDto.class,
                        author.authorNo,
                        author.authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetAuthorResponseDto> getAuthorsByProductNo(Long productNo) {
        QAuthor author = QAuthor.author;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QProduct product = QProduct.product;

        return from(author)
                .innerJoin(productAuthor.author, author)
                .innerJoin(product, productAuthor.product)
                .where(product.productNo.eq(productNo))
                .select(Projections.constructor(GetAuthorResponseDto.class, author.authorNo, author.authorName))
                .fetch();
    }
}
