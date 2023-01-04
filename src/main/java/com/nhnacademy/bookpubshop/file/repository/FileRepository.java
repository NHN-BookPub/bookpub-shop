package com.nhnacademy.bookpubshop.file.repository;

import com.nhnacademy.bookpubshop.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface FileRepository extends JpaRepository<File, Long> {
}
