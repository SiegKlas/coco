package ru.michael.coco.file;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.michael.coco.user.UserRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    @Value("${file.solutions}")
    private String solutionsDir;

    @Autowired
    public UploadController(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @PostMapping("/solution")
    public String handleSolutionUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam("dir_name") String dirName) {
        try {
            String fullPath = FileManager.moveMultipartFileToSubfolder(Path.of(solutionsDir),
                    userDetails.getUsername(), file);

            String otherServerUrl = "http://127.0.0.1:5000/api/check";
            RestTemplate restTemplate = new RestTemplate();
            FileEntity fileEntity = new FileEntity(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    fullPath,
                    userRepository.findByUsername(userDetails.getUsername()).orElseThrow(),
                    LocalDateTime.now()
            );
            fileRepository.save(fileEntity);
            final Map<String, String> json = new HashMap<>();
            json.put("fileName", file.getOriginalFilename());
            json.put("filePath", fullPath);
            json.put("dirName", dirName);
            ResponseEntity<String> response = restTemplate.postForEntity(otherServerUrl, json, String.class);
            System.out.println(response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
