package com.project1.fileSystem;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @PostMapping("/pdf")
    public ResponseEntity<DocDTO> uploadPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            DocDTO docDTO = docService.storeDoc(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(docDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DocDTO(null, "Failed to upload file: " + e.getMessage()));
        }
    }

    @GetMapping("/pdf/{fileName}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("fileName") String fileName) {
        try {
            byte[] pdfBytes = docService.getPdf(fileName);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
