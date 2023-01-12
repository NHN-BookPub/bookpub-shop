package com.nhnacademy.bookpubshop.author.repository.impl;

import com.nhnacademy.bookpubshop.author.dto.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.entity.QAuthor;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepositoryCustom;
import com.nhnacademy.bookpubshop.product.entity.QProduct;
import com.nhnacademy.bookpubshop.product.relationship.entity.QProductAuthor;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 저자 Custom repository의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 */
public class AuthorRepositoryImpl extends QuerydslRepositorySupport
        implements AuthorRepositoryCustom {
    private final EntityManager entityManager;
    /**
     * 생성자입니다.
     */
    public AuthorRepositoryImpl(EntityManager entityManager) {
        super(Author.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetAuthorResponseDto> getAuthorsByPage(Pageable pageable) {
        QAuthor author = QAuthor.author;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<GetAuthorResponseDto> query = queryFactory
                .from(author)
                .select(Projections.constructor(GetAuthorResponseDto.class,
                        author.authorNo,
                        author.authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(author.count()).from(author).fetchOne();

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> count);
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

    @Override
    public List<GetAuthorResponseDto> getAuthorByName(String name) {
        QAuthor author = QAuthor.author;

        return from(author)
                .select(Projections.constructor(
                        GetAuthorResponseDto.class,
                        author.authorNo,
                        author.authorName
                ))
                .where(author.authorName.eq(name))
                .fetch();
    }
}
