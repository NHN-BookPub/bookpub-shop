package com.nhnacademy.bookpubshop.annotation.aspect;

import static com.nhnacademy.bookpubshop.auth.Authorization.ROLE_ADMIN;
import static com.nhnacademy.bookpubshop.auth.Authorization.ROLE_MEMBER;

import com.nhnacademy.bookpubshop.auth.Authorization;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Header 에 정보를 받아서 인가하기위한 AOP 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/

@Slf4j
@Aspect
@Component
public class AuthorizationPointCut {

    private static final String AUTH_HEADER = "X-Authorization-Roles";
    public static final String AUTH_MEMBER_INFO = "X-Authorization-Id";

    /**
     * Member 에대한 권한과 값을 검사하기위한 Aop 설정입니다.
     *
     * @param pjp joinPoint 값 기입
     * @return the object
     */
    @Around(value = "@annotation(com.nhnacademy.bookpubshop.annotation.MemberAuth)")
    public Object checkMemberAuthorization(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();
        if (getHeader(request, AUTH_MEMBER_INFO, null)) {
            log.error("헤더가 없음");
            return httpResponse(HttpStatus.UNAUTHORIZED);
        }

        if (!request.getRequestURI().contains(request.getHeader(AUTH_MEMBER_INFO))) {
            log.error("Url path 에 멤버 no 가 없음");
            return httpResponse(HttpStatus.UNAUTHORIZED);
        }

        if (getHeader(request, AUTH_HEADER, ROLE_MEMBER)) {
            return pjp.proceed(pjp.getArgs());

        }
        return httpResponse(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 관리자 권한만 들어올수있게 처리하는 AOP 입니다.
     *
     * @param pjp joinPoint 기입
     * @return the object
     */
    @Around(value = "@annotation(com.nhnacademy.bookpubshop.annotation.AdminAuth)")
    public Object checkAdminAuthorization(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();

        if (getHeader(request, AUTH_HEADER, ROLE_ADMIN)) {
            return pjp.proceed(pjp.getArgs());
        }

        return httpResponse(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 어떠한 http 가들어왔을때 반환하는 메서드 입니다.
     *
     * @param status httpStatus 값이 기입됩니다.
     * @return status 에 맞는 responseEntity 가 반환됩니다.
     */
    private static ResponseEntity<Object> httpResponse(HttpStatus status) {
        return ResponseEntity.status(status)
                .build();
    }

    /**
     * 회원과 관리자 권한이 둘다 통과시키는 메서드입니다.
     *
     * @param pjp the pjp
     * @return the object
     */
    @Around(value = "@annotation(com.nhnacademy.bookpubshop.annotation.MemberAndAuth)")
    public Object checkAdminAndMemberAuthorization(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = getRequest();
        if (getHeader(request, AUTH_HEADER, ROLE_ADMIN)) {
            return pjp.proceed(pjp.getArgs());
        }

        if (getHeader(request, AUTH_MEMBER_INFO, null)) {
            log.error("헤더가 없음");
            return httpResponse(HttpStatus.UNAUTHORIZED);
        }

        if (!request.getRequestURI().contains(request.getHeader(AUTH_MEMBER_INFO))) {
            log.error("Url path 에 멤버 no 가 없음");
            return httpResponse(HttpStatus.UNAUTHORIZED);
        }
        if (getHeader(request, AUTH_HEADER, ROLE_MEMBER)) {
            return pjp.proceed(pjp.getArgs());

        }
        return httpResponse(HttpStatus.UNAUTHORIZED);
    }

    /**
     * RequestContextHolder 에서 현재의 요청을받아.
     * HttpServletRequest 반환해주는 메소드.
     *
     * @return httpServletRequest 가 반환된다.
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes
                = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        return servletRequestAttributes.getRequest();
    }

    /**
     * 특정 헤더값을 통해 그 헤더에가 있는지를 확인하기위한 메서드입니다.
     *
     * @param request       httpServletRequest 가 들어온다.
     * @param header        어떤 헤더종류
     * @param authorization Enum 타입의 어떠한 값 (MEMBER, ADMIN)
     * @return boolean
     */
    private static boolean getHeader(HttpServletRequest request,
                                     String header,
                                     Authorization authorization) {
        if (Objects.isNull(authorization)) {
            return request.getHeader(header).isEmpty();
        }

        return request.getHeader(header)
                .contains(authorization.name());
    }
}
