package com.example.gcp_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileService {


    private final Path storageLocation;


    public FileService(@Value("${file.storage.location:./data}") String storageDir) throws IOException {
        this.storageLocation = Paths.get(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(this.storageLocation);
    }


    public String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if(filename.contains("..")) {
            throw new IOException("Invalid path sequence in filename " + filename);
        }
        Path target = this.storageLocation.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }


    public byte[] readFile(String filename) throws IOException {
        Path filePath = this.storageLocation.resolve(filename).normalize();
        if(!Files.exists(filePath)) throw new IOException("File not found: " + filename);
        return Files.readAllBytes(filePath);
    }
}
