package com.nhnacademy.bookpubshop.author.dummy;

import com.nhnacademy.bookpubshop.author.entity.Author;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class AuthorDummy {

    public static Author dummy() {
        return new Author(null,
                "author", "해리포터");
    }
}
