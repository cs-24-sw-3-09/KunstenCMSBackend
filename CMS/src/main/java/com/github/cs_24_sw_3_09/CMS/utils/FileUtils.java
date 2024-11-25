package com.github.cs_24_sw_3_09.CMS.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtils {
    public static File createVisualMediaFile(MultipartFile file) throws IOException {
        File newFile = createFileFromRoot("/files/visual_media/" + file.getOriginalFilename());
        file.transferTo(newFile);
        return newFile;
    }

    /**
     * @param path is the path relative the project root folder.
     * @return Returns the file from the specified path.
     */
    public static File createFileFromRoot(String path) {
        String absPath = String.format("%s/%s", System.getProperty("user.dir"), path);
        return new File(absPath);
    }
}
