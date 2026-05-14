package com.sam.library.student.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    String upload(MultipartFile file);
    List<String> uploadMultiple(List<MultipartFile> files);
    void delete(String fileName);
    List<String> listFiles();
}
