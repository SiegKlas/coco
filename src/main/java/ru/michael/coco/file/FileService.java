package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public void saveFiles(List<MultipartFile> files, Long userId) throws IOException {
        for (MultipartFile file : files) {
            saveFile(file, userId);
        }
    }

    public void saveFile(MultipartFile file, Long userId) throws IOException {
        // Генерируем уникальное имя файла
        String fileName = file.getOriginalFilename();
        // Путь к директории, где будут храниться загруженные файлы
        Path uploadPath = Path.of(uploadDir);
        // Создаем директорию, если её нет
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Полный путь к файлу
        Path filePath = uploadPath.resolve(fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Создаем запись в базе данных
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setUser(user);
        fileEntity.setUploadTime(LocalDateTime.now());

        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getFilesByUser(Long userId) {
        // Получение списка файлов по пользователю
        return fileRepository.findAllByUser_Id(userId);
    }
}
