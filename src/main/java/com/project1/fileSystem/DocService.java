package com.project1.fileSystem;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DocService {

    private final String pdfUploadDirectory = "upload/pdfs";
    private final DocRepository docRepository;
    private final DocMapper docMapper;
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    @Transactional
    public DocDTO storeDoc (MultipartFile file) throws IOException {

        if (!isPdf(file)) {
            throw new IllegalArgumentException("File is not a PDF");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path directory = Paths.get(pdfUploadDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Doc doc = new Doc();
        doc.setDoc(fileName);
        doc = docRepository.save(doc);

        return docMapper.toDto(doc);
    }

    public byte[] getPdf(String fileName) throws IOException {
        Path filePath = Paths.get(pdfUploadDirectory, fileName);
        return Files.readAllBytes(filePath);
    }

    private boolean isPdf(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals(PDF_CONTENT_TYPE);
    }

}
