package com.pethealthcloud.pethealthcloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        // 建立 uploads 資料夾
        File uploadDir = new File("uploads");

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 產生唯一檔名
        String fileName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 存檔
        File destination =
                new File(uploadDir, fileName);

        System.out.println(destination.getAbsolutePath());

        file.transferTo(destination.getAbsoluteFile());

        // 回傳圖片路徑
        return ResponseEntity.ok(
                "uploads/" + fileName
        );
    }

}