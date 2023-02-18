package com.nhnacademy.bookpubshop.sales.dummy;

import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TotalSaleDummy {

    public static TotalSaleDto totalSaleDummy() {
        return new TotalSaleDto(1L, 1L,
                1L, 1L, 1L, 1L);

    }

    public static OrderCntResponseDto orderCntDummy() {
        return new OrderCntResponseDto(1, 1L);
    }

    public static TotalSaleYearDto totalSaleYearDummy() {
        return new TotalSaleYearDto(1L, 1L,
                1L, 1L, 1L, 1L, 1);
    }
}
