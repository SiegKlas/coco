package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;  // Добавлено поле userRepository

    @GetMapping
    public String listFiles(Model model, Principal principal) {
        // Получение текущего пользователя из контекста Spring Security
        Long userId = getUserIdFromPrincipal(principal);
        model.addAttribute("files", fileService.getFilesByUser(userId));
        return "fileList";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        // Получение текущего пользователя из контекста Spring Security
        Long userId = getUserIdFromPrincipal(principal);
        fileService.saveFile(file, userId);
        return "redirect:/files";
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        // Получение текущего пользователя из контекста Spring Security
        String username = principal.getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}