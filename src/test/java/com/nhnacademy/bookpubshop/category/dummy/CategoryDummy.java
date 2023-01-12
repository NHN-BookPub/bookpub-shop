package com.nhnacademy.bookpubshop.category.dummy;

import com.nhnacademy.bookpubshop.category.entity.Category;

/**
 * Some description here.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public class CategoryDummy {

    public static Category dummy() {
        return new Category(
                null,
                null,
                "name",
                0,
                true
        );
    }
}
