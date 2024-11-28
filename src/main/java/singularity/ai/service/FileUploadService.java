package singularity.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import singularity.ai.model.UploadedFile;
import singularity.ai.repository.UploadedFileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class FileUploadService {

    private final UploadedFileRepository fileRepository;

    // Определите директорию для сохранения файлов
    private static final String UPLOAD_DIR = "uploads/";

    public FileUploadService(UploadedFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public UploadedFile saveFile(MultipartFile file) throws IOException {
        // Убедитесь, что директория существует
        Path uploadDir = Path.of(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Генерация пути для сохранения файла
        String fileName = file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        // Сохранение файла на диск
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Создание объекта UploadedFile
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(fileName);
        uploadedFile.setFilePath(filePath.toString());
        uploadedFile.setMediaType(file.getContentType());
        uploadedFile.setFileSize(file.getSize());
        uploadedFile.setData(file.getBytes()); // Храним байтовые данные файла
        uploadedFile.setUploadTime(LocalDateTime.now());

        return fileRepository.save(uploadedFile);
    }
}
