package com.nhnacademy.bookpubshop.utils;

import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.utils.exception.FileException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일을 읽어 처리하는 Util 클래스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public final class FileUtils {

    @Value("${file.save.path}")
    private static String basePath;

    private FileUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * 파일을 저장하는 메소드입니다.
     *
     * @param name 카테고리명
     * @param file 파일
     * @return 저장 위치 반환
     */
    public static String saveFile(String name, MultipartFile file) {

        String uuid = UUID.randomUUID().toString();
        int pos = file.getName().indexOf(".");
        String fileName = file.getName().substring(0, pos);
        String fileExtension = file.getName().substring(pos);

        try (
                FileOutputStream fos = new FileOutputStream(basePath + uuid);
                InputStream is = file.getInputStream()
        ) {
            int readCount;
            byte[] buffer = new byte[1024];
            while ((readCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readCount);
            }
        } catch (Exception ex) {
            throw new FileException();
        }

        new File(null, null, null, null, null, null,
                name, basePath, fileExtension, fileName, uuid);

        return basePath + uuid;
    }
}
