package com.example.gcp_app.controller;

import com.example.gcp_app.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@RestController
@RequestMapping("/api/files")
@Validated
public class FileController {


    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    // 1) Fetch file/data
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> fetchFile(@PathVariable String filename) throws IOException {
        byte[] data = fileService.readFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }


    // 2) Save/upload file
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String stored = fileService.storeFile(file);
        return ResponseEntity.ok("Stored as: " + stored);
    }


    // 3) Simple health check (could be replaced by /actuator/health)
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
