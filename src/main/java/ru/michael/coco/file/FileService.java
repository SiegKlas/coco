package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public Long getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    // admin only
    public void saveFiles(List<MultipartFile> files, Long userId) throws IOException {
        for (MultipartFile file : files) {
            saveFile(file, userId);
        }
    }

    public void saveFile(MultipartFile file, Long userId) throws IOException {
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).replace("exfiles_copy/", "");
        Path uploadPath = Path.of(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileEntity fileEntity = new FileEntity(fileName, file.getContentType(), filePath.toString(), user,
                LocalDateTime.now());

        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getFilesByUser(Long userId) {
        return fileRepository.findAllByUser_Id(userId);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
