package com.github.cs_24_sw_3_09.CMS.utils;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component
public class FileUtils {
    public static File createVisualMediaFile(MultipartFile file, String newFileName) throws IOException {

        File newFile = createFileFromRoot("files/visual_media", newFileName + mimeToType(file.getContentType()));
        file.transferTo(newFile);
        return newFile;
    }

    /**
     * @param path is the path relative the project root folder.
     * @return Returns the file from the specified path.
     */
    public static File createFileFromRoot(String path, String file) {
        try {
            Files.createDirectories(Paths.get(String.format("%s/%s", System.getProperty("user.dir"), path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String absPath = String.format("%s/%s/%s", System.getProperty("user.dir"), path, file);
        return new File(absPath);
    }




    public static String mimeToType(String mime) {
        if (mime == null) {
            return ".unknown"; // Default for unknown MIME types
        }

        switch (mime) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            case "image/bmp":
                return ".bmp";
            case "video/mp4":
                return ".mp4";
            default:
                return ".unknown"; // Default for unrecognized MIME types
        }
    }

    public static void removeVisualMediaFile(VisualMediaEntity visualMediaEntity) {
        // Create the file object for the file to be deleted
        File fileToDelete = createFileFromRoot(
                "files/visual_media",
                visualMediaEntity.getId() + mimeToType(visualMediaEntity.getFileType())
        );

        // Attempt to delete the file
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (!deleted) {
                System.err.println("Failed to delete file: " + fileToDelete.getAbsolutePath());
            } else {
            }
        } else {
            System.err.println("File does not exist: " + fileToDelete.getAbsolutePath());
        }
    }
}
