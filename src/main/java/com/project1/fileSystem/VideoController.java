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
public class VideoController {

    private final VideoService videoService;
    @PostMapping("/video")
    public ResponseEntity<VideoDTO> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            VideoDTO videoDTO = videoService.storeVideo(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(videoDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VideoDTO(null, "Failed to upload file: " + e.getMessage()));
        }
    }
    @GetMapping("/video/{videoName}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable("videoName") String videoName) {
        try {
            byte[] videoBytes = videoService.getVideo(videoName);
            ByteArrayResource resource = new ByteArrayResource(videoBytes);

            return ResponseEntity.ok()
                    //HERE
                    .contentType(MediaType.valueOf("video/mp4"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
