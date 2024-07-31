package com.project1.fileSystem;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VideoService {
    private static final double MAX_DURATION_SECONDS = 61.0;
    private final String videoUploadDirectory = "upload/videos";
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Transactional
    public VideoDTO storeVideo(MultipartFile file) throws IOException {

        Path tempFilePath = Files.createTempFile(null, null);
        Files.write(tempFilePath, file.getBytes());

        try {
            File tempFile = tempFilePath.toFile();
            MultimediaObject multimediaObject = new MultimediaObject(tempFile);
            MultimediaInfo info = multimediaObject.getInfo();
            long duration = info.getDuration();
            double durationInSeconds = (double) duration / 1000;

            if (durationInSeconds > MAX_DURATION_SECONDS) {
                throw new IllegalArgumentException("Video duration exceeds the maximum limit of 60 seconds");
            }
        } catch (EncoderException e) {
            throw new IllegalArgumentException("Failed to process the video file", e);
        } finally {
            Files.deleteIfExists(tempFilePath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path directory = Paths.get(videoUploadDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Video video = new Video();
        video.setVideo(fileName);
        video = videoRepository.save(video);

        VideoDTO videoDTO = videoMapper.toDto(video);
        return videoDTO;
    }

    public byte[] getVideo(String videoName) throws IOException {
        Path filePath = Paths.get(videoUploadDirectory, videoName);
        return Files.readAllBytes(filePath);
    }


}
