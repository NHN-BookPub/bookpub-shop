package com.nhnacademy.bookpubshop.point.repository;

import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 포인트내역 레포지토리입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PointHistoryRepository
        extends JpaRepository<PointHistory, Long>, PointHistoryRepositoryCustom {

}
