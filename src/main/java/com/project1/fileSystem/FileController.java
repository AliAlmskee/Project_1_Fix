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

 //  @PreAuthorize("hasRole('ADMIN')")
 @PostMapping("/photo")
 public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("file") MultipartFile file) {
     if (file.isEmpty()) {
         return ResponseEntity.badRequest().build();
     }

     try {
         PhotoDTO photoDTO = fileService.storePhoto(file);
         return ResponseEntity.status(HttpStatus.CREATED).body(photoDTO);
     } catch (IOException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PhotoDTO(null, "Failed to upload file: " + e.getMessage()));
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
