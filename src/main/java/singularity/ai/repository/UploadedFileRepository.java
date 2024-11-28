package singularity.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.ai.model.UploadedFile;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    UploadedFile findByFileName(String fileName);
}

