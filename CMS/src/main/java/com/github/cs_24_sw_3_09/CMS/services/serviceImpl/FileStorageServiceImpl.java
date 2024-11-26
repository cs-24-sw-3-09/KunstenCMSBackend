package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.services.FileStorageService;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    @Override
    public void saveFile(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println("imgere");
        FileUtils.createVisualMediaFile(file);
        System.out.println("done");

    }
}
