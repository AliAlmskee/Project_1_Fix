package com.project1.fileSystem;


import com.project1.profile.ClientProfileService;
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
public class FileService {

    private final String photoUploadDirectory = "upload/photos";
    private final PhotoRepository photoRepository;
    private PhotoMapper photoMapper;

    @Transactional
    public PhotoDTO storePhoto(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path directory = Paths.get(photoUploadDirectory);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Photo photo = new Photo();
        photo.setPhoto(fileName);
        photo = photoRepository.save(photo);

        PhotoDTO photoDTO = photoMapper.toDto(photo);
        return photoDTO;
    }

    public byte[] getPhoto(String photoName) throws IOException {
        Path filePath = Paths.get(photoUploadDirectory, photoName);
        byte[] images = Files.readAllBytes(filePath);
        return images;
    }
}