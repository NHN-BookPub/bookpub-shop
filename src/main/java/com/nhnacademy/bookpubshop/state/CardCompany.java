package com.nhnacademy.bookpubshop.state;

import com.nhnacademy.bookpubshop.card.exception.NotSupportedCompanyException;
import java.util.Arrays;
import lombok.Getter;

/**
 * 카드 회사.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
public enum CardCompany {
    IBK("기업비씨", "3K"),
    GWANGJUBANK("광주은행", "46"),
    LOTTE("롯데카드", "71"),
    KDBBANK("KDB산업은행", "30"),
    BC("비씨카드", "31"),
    SAMSUNG("삼성카드", "51"),
    SAEMAUL("새마을금고", "38"),
    SHINHAN("신한카드", "41"),
    SHINHYEOP("신협", "62"),
    CITI("씨티카드", "36"),
    WOORI("우리카드", "33"),
    POST("우체국예금보험", "37"),
    SAVINGBANK("저축은행중앙회", "39"),
    JEONBUKBANK("씨티카전북은행드", "35"),
    JEJUBANK("제주은행", "42"),
    KAKAOBANK("카카오뱅크", "15"),
    KBANK("케이뱅크", "3A"),
    TOSSBANK("토스뱅크", "24"),
    CITHANAI("하나카드", "21"),
    KOOKMIN("KB국민카드", "11"),
    NONGHYEOP("NH농협카드", "91"),
    SUHYEOP("Sh수협은행", "34");

    private final String name;
    private final String code;

    CardCompany(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 가지고 있는 회사 목록중에 해당하는 회사를 반환시켜주는 메소드.
     *
     * @param code 회사 코드.
     * @return 회사정보.
     */
    public static CardCompany match(String code) {
        return Arrays.stream(values())
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElseThrow(NotSupportedCompanyException::new);
    }
}
