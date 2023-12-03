package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveFile(MultipartFile file, Long userId) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setData(file.getBytes());

        // Получение пользователя из базы данных
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        fileEntity.setUser(user);
        fileEntity.setUploadTime(LocalDateTime.now());
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getFilesByUser(Long userId) {
        // Получение списка файлов по пользователю
        return fileRepository.findAllByUser_Id(userId);
    }
}
