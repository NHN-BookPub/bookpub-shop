package com.nhnacademy.bookpubshop.dummy;

import com.nhnacademy.bookpubshop.tier.entity.Tier;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class TierDummy {
    public static Tier dummy() {
        return new Tier(null, "tier");
    }
}
