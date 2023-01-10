package com.nhnacademy.bookpubshop.tag.dummy;

import com.nhnacademy.bookpubshop.tag.entity.Tag;

/**
 * 태그 더미.
 *
 * @author : 박경서
 * @since : 1.0
 **/

public class TagDummy {

    public static Tag dummy() {
        return new Tag(
                null,
                "강추",
                "#FFFFFF"
        );
    }
}
