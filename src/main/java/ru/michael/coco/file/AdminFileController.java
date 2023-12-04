package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/files")
public class AdminFileController {
    private final FileService fileService;

    @Autowired
    public AdminFileController(FileService fileService) {
        this.fileService = fileService;
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
