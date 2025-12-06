package com.apistore.controller;

import com.apistore.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile[] files) {
        try {
            List<Map<String, Object>> uploadedImages = new ArrayList<>();

            for (MultipartFile file : files) {
                Map result = cloudinaryService.upload(file);

                uploadedImages.add(Map.of(
                        "url", result.get("secure_url"),
                        "public_id", result.get("public_id")
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "images", uploadedImages
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Upload thất bại",
                            "error", e.getMessage()
                    ));
        }
    }
}

