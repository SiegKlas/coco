package ru.michael.coco.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileServingController {
    @Value("${file.xbank-dir}")
    private String xbankDir;
    @Value("${file.solutions}")
    private String solutionsDir;

    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(xbankDir + '/' + filename).resolve(filename + ".pdf");
            Resource fileResource = new InputStreamResource(new FileInputStream(file.toFile()));

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/asm/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(xbankDir + '/' + filename).resolve(filename + ".asm");
            Resource fileResource = new InputStreamResource(new FileInputStream(file.toFile()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + ".asm" + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);

        } catch (FileNotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/solution/{filename:.+}")
    public ResponseEntity<Resource> downloadSolution(@PathVariable String filename,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            Path file = Paths.get(solutionsDir + '\\' + username + '\\' + filename);
            Resource fileResource = new InputStreamResource(new FileInputStream(file.toFile()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);

        } catch (FileNotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
