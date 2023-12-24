package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.michael.coco.user.UserRepository;

import java.security.Principal;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserRepository userRepository;

    @Autowired
    public FileController(FileService fileService, UserRepository userRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listFiles(Model model, Principal principal) {
        Long userId = fileService.getUserIdFromPrincipal(principal);
        model.addAttribute("files", fileService.getFilesByUser(userId));
        return "fileList";
    }
}