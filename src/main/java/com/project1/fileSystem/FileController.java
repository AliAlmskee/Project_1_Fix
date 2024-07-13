package com.project1.fileSystem;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

   @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/photo")
    public ResponseEntity<UploadResponse> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            UploadResponse errorResponse = new UploadResponse("Please select a file to upload");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }

        try {
            String fileName = fileService.storePhoto(file);
            UploadResponse successResponse = new UploadResponse(fileName);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(successResponse);
        } catch (IOException e) {
            UploadResponse errorResponse = new UploadResponse("Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

   @GetMapping("/photo/{photoName}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable("photoName") String photoName) {
        try {
            byte[] photoBytes = fileService.getPhoto(photoName);
            ByteArrayResource resource = new ByteArrayResource(photoBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photoName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
