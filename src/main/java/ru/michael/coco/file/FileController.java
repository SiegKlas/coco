package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String listFiles(Model model, Principal principal) {
        Long userId = fileService.getUserIdFromPrincipal(principal);
        model.addAttribute("files", fileService.getFilesByUser(userId));
        return "fileList";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("file") List<MultipartFile> files, Principal principal) throws IOException {
        Long userId = fileService.getUserIdFromPrincipal(principal);
        fileService.saveFiles(files, userId);
        return "redirect:/files";
    }
}
