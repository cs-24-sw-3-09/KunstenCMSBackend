package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.dao.IVisualMediaDao;
import com.github.cs_24_sw_3_09.CMS.dao.daoImpl.VisualMediaDaoImpl;
import com.github.cs_24_sw_3_09.CMS.services.IVisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class VisualMediaServiceImpl implements IVisualMediaService {

    private final IVisualMediaDaogit visualMediaDao;

    @Autowired
    public VisualMediaServiceImpl(IVisualMediaDao visualMediaDao) {
        this.visualMediaDao = visualMediaDao;
    }

    @Override
    public void createVisualMedia(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }
        FileUtils.createVisualMediaFile(file);

        /* Add to database */

    }

    @Override
    public ResponseEntity<Object> deleteVisualMedia(String path) {

        /* Delete file from folder */
        File fileToDelete = FileUtils.createFileFromRoot(path);

        if (!fileToDelete.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        if (!fileToDelete.delete()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete file");
        }

        /* Update in database */

        return ResponseEntity.status(HttpStatus.OK).body("File successfully deleted");
    }
}
