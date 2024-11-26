package com.github.cs_24_sw_3_09.CMS.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    void saveVisualMediaFile(MultipartFile file, String newFileName) throws IOException;

}
