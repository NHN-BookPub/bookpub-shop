package com.nhnacademy.bookpubshop.author.repository;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.NEW;
import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.author.dto.response.GetAuthorResponseDto;
import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

/**
 * 저자 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorRepository authorRepository;

    Author author;

    @BeforeEach
    void setUp() {
        author = AuthorDummy.dummy();
    }

    @Test
    @DisplayName("저자 save 테스트")
    void memberSaveTest() {
        Author persist = authorRepository.save(author);
        entityManager.persist(author);

        Optional<Author> author = authorRepository.findById(persist.getAuthorNo());

        assertThat(author).isPresent();
        assertThat(author.get().getAuthorNo()).isEqualTo(persist.getAuthorNo());
        assertThat(author.get().getAuthorName()).isEqualTo(persist.getAuthorName());
        assertThat(author.get().getMainBook()).isEqualTo(persist.getMainBook());
    }


    @Test
    @DisplayName("모든 저자 찾기")
    void getAuthorsByPageTest() {
        Author persist = entityManager.persist(author);

        GetAuthorResponseDto responseDto = new GetAuthorResponseDto(1, "사람", "해리포터");

        List<GetAuthorResponseDto> responses = new ArrayList<>();
        responses.add(responseDto);

        org.springframework.data.domain.Pageable pageable = Pageable.ofSize(5);

        assertThat(authorRepository.getAuthorsByPage(pageable)
                .getContent().get(0).getAuthorNo())
                .isEqualTo(persist.getAuthorNo());

        assertThat(authorRepository.getAuthorsByPage(pageable)
                .getContent().get(0).getAuthorName())
                .isEqualTo(persist.getAuthorName());
    }

    @Test
    @DisplayName("상품 번호로 저자 찾기")
    void getAuthorsByProductNo() {
        Author persist = entityManager.persist(author);
        ProductPolicy productPolicy = new ProductPolicy(null, "method", true, 1);
        ProductTypeStateCode typeStateCode = new ProductTypeStateCode(null, BEST_SELLER.getName(), BEST_SELLER.isUsed(), "info");
        ProductSaleStateCode saleStateCode = new ProductSaleStateCode(null, NEW.getName(), NEW.isUsed(), "info");

        ProductPolicy policyPersist = entityManager.persist(productPolicy);
        ProductTypeStateCode typePersist = entityManager.persist(typeStateCode);
        ProductSaleStateCode salePersist = entityManager.persist(saleStateCode);

        Product product = new Product(null,
                policyPersist,
                typePersist,
                salePersist,
                null,
                "1231231233",
                "test",
                "publisher",
                130,
                "test_description",
                10000L,
                10000L,
                0,
                0L,
                10,
                false,
                100,
                LocalDateTime.now(),
                false);

        Product productPersist = entityManager.persist(product);

        ProductAuthor productAuthor = new ProductAuthor(
                new ProductAuthor.Pk(persist.getAuthorNo(), productPersist.getProductNo()),
                persist, productPersist);

        entityManager.persist(productAuthor);

        assertThat(authorRepository.getAuthorsByProductNo(productPersist.getProductNo())
                .get(0).getAuthorName())
                .isEqualTo(persist.getAuthorName());

        assertThat(authorRepository.getAuthorsByProductNo(productPersist.getProductNo()).get(0).getAuthorNo())
                .isEqualTo(persist.getAuthorNo());
    }

    @Test
    @DisplayName("저자 이름으로 찾기")
    void getAuthorByName() {
        Author persist = entityManager.persist(author);

        assertThat(authorRepository.getAuthorByName(author.getAuthorName()).get(0).getAuthorName())
                .isEqualTo(persist.getAuthorName());
    }
}