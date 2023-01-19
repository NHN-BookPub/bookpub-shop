package com.nhnacademy.bookpubshop.order.repository;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface OrderRepository extends JpaRepository<BookpubOrder, Long>, OrderRepositoryCustom {

}
