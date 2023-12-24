package ru.michael.coco.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileManager {
    public static void moveMultipartFileToSubfolder(Path path, String subfolderName, MultipartFile multipartFile) throws IOException {
        Path subfolderPath = path.resolve(subfolderName);

        if (!Files.exists(subfolderPath)) {
            Files.createDirectories(subfolderPath);
        }

        String originalFileName = multipartFile.getOriginalFilename();
        Path tempFile = Files.createTempFile(null, originalFileName);
        multipartFile.transferTo(tempFile.toFile());

        String fileName = originalFileName;
        String fileExtension = "";

        assert fileName != null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = fileName.substring(i);
            fileName = fileName.substring(0, i);
        }

        Path newFilePath = subfolderPath.resolve(fileName + fileExtension);
        int counter = 1;
        while (Files.exists(newFilePath)) {
            newFilePath = subfolderPath.resolve(fileName + "-" + counter + fileExtension);
            counter++;
        }

        Files.move(tempFile, newFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
