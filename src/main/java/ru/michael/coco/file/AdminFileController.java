package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/files")
public class AdminFileController {
    private final FileService fileService;

    @Autowired
    public AdminFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("file") List<MultipartFile> files, Principal principal) throws IOException {
        Long userId = fileService.getUserIdFromPrincipal(principal);
        fileService.saveFiles(files, userId);
        return "redirect:/admin/files";
    }

    @GetMapping
    public String listAllFiles(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
        return "adminFiles";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return "redirect:/admin/files";
    }
}
