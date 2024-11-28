package singularity.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import singularity.ai.model.UploadedFile;
import singularity.ai.service.FileUploadService;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            UploadedFile savedFile = fileUploadService.saveFile(file);
            return ResponseEntity.ok("File uploaded successfully with ID: " + savedFile.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }
}
