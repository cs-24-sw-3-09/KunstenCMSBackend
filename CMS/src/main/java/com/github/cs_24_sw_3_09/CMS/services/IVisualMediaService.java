package com.github.cs_24_sw_3_09.CMS.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IVisualMediaService {
    public void createVisualMedia(MultipartFile file) throws IOException;
    public void deleteVisualMedia(String path);
}
