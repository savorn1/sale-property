package com.sam.library.student.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.service.FileUploadService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/files")
@Tag(name = "File", description = "File upload APIs (MinIO)")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam MultipartFile file) {
        String url = fileUploadService.upload(file);
        return ResponseEntity.status(201).body(ApiResponse.success("File uploaded", url));
    }

    @PostMapping(value = "/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<String>>> uploadMultiple(@RequestParam List<MultipartFile> files) {
        List<String> urls = fileUploadService.uploadMultiple(files);
        return ResponseEntity.status(201).body(ApiResponse.success("Files uploaded", urls));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String fileName) {
        fileUploadService.delete(fileName);
        return ResponseEntity.ok(ApiResponse.success("File deleted", fileName));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> listFiles() {
        return ResponseEntity.ok(ApiResponse.success(fileUploadService.listFiles()));
    }
}
