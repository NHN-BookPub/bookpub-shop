package com.nhnacademy.bookpubshop.file.repository;

import com.nhnacademy.bookpubshop.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * File 테이블에 있는값을 가져오기위한 Repo 인터페이스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFilePath(String path);

    boolean existsByFilePath(String path);
}
