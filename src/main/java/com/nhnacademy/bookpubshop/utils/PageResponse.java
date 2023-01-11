package com.nhnacademy.bookpubshop.utils;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * 페이지 공통 응답객체 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;

    private int totalPages;

    private int number;

    private boolean previous;

    private boolean next;

    /**
     * Page 값을 입력받아서 응답객체를 만드는 생성자입니다.
     *
     * @param result the result
     */
    public PageResponse(Page<T> result) {

        this.content = result.getContent();

        this.totalPages = result.getTotalPages();

        this.number = result.getNumber();

        this.previous = result.hasPrevious();

        this.next = result.hasNext();
    }
}
