package ru.michael.coco.file;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final UserRepository userRepository;
    @Value("${file.solutions}")
    private String solutionsDir;

    @Autowired
    public UploadController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/solution")
    public String handleSolutionUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        try {
            FileManager.moveMultipartFileToSubfolder(Path.of(solutionsDir), userDetails.getUsername(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
