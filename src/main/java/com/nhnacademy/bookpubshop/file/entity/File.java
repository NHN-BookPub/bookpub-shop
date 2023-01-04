package com.nhnacademy.bookpubshop.file.entity;

import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.service.entity.CustomerService;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일개체 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "file")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_number")
    private Long fileNo;

    @ManyToOne
    @JoinColumn(name = "personal_inquiry_number")
    private PersonalInquiry personalInquiry;

    @ManyToOne
    @JoinColumn(name = "customer_service_number")
    private CustomerService customerService;

    @Column(name = "file_category")
    private String fileCategory;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_name_origin")
    private String nameOrigin;

    @Column(name = "file_name_saved")
    private String nameSaved;

    @Column(name = "file_created_at")
    private LocalDateTime createdAt;
}
