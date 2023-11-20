package com.ndt.be_stepupsneaker.infrastructure.file.controller;

import com.ndt.be_stepupsneaker.infrastructure.file.service.StorageService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public Object uploadFile(@RequestParam(value = "file") MultipartFile file) {
        String folder = "NEW FOLDER/";
        return ResponseHelper.getResponse(storageService.uploadFile(file, folder), HttpStatus.OK);
    }
//    @PostMapping("/uploads")
//    public Object uploadFiles(@RequestParam(value = "file") MultipartFile[] file) {
//        String folder = "NEW FOLDER/";
//        return ResponseHelper.getResponse(storageService.uploadFiles(file), HttpStatus.OK);
//    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource byteArrayResource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(byteArrayResource);
    }

    @DeleteMapping("/delete/{fileName}")
    public Object deleteFile(@PathVariable("fileName") String fileName) {
        return ResponseHelper.getResponse(storageService.deleteFile(fileName), HttpStatus.OK);
    }
}
