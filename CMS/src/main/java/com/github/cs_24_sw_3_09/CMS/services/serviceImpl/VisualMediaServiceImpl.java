package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.services.IVisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class VisualMediaServiceImpl implements IVisualMediaService {
    @Override
    public void createVisualMedia(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }
        File uploadedFile = FileUtils.createVisualMediaFile(file);


        /* Add to database */
    }

    @Override
    public void deleteVisualMedia(String path) {

    }
}
