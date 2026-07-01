package com.pethealthcloud.pethealthcloud.controller;

import com.pethealthcloud.pethealthcloud.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.pethealthcloud.pethealthcloud.dto.PetRequest;
import jakarta.validation.Valid;
import com.pethealthcloud.pethealthcloud.dto.PetResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.pethealthcloud.pethealthcloud.repository.UserRepository;
import com.pethealthcloud.pethealthcloud.config.JwtUtil;
import com.pethealthcloud.pethealthcloud.entity.User;

    @Tag(
        name = "Pet API",
        description = "寵物管理相關 API"
    )
    @RestController
    public class PetController {

    private final PetService petService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public PetController(
            PetService petService,
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.petService = petService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "取得所有寵物",
            description = "查詢系統內所有寵物資料"
    )
    @ApiResponse(
            responseCode = "200",
            description = "成功取得所有寵物資料"
    )
    @GetMapping("/pets")
    public ResponseEntity<List<PetResponse>> getPets(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtUtil.getEmailFromToken(token);

        User user =
                userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("找不到使用者");
        }

        return ResponseEntity.ok(
                petService.getAllPets(user.getId())
        );
    }
    @Operation(
            summary = "新增寵物",
            description = "建立新的寵物資料"
    )
    @PostMapping("/pets")
    public ResponseEntity<PetResponse> addPet(
            @Valid @RequestBody PetRequest petRequest,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtUtil.getEmailFromToken(token);

        User user =
                userRepository.findByEmail(email);
        if(user == null) {
            throw new RuntimeException("找不到使用者");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        petService.addPet(
                                petRequest,
                                user
                        )
                );
    }

    @Operation(
            summary = "刪除寵物",
            description = "根據 ID 刪除寵物資料"
    )
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token =
                authHeader.replace("Bearer " , "");

        String email =
                jwtUtil.getEmailFromToken(token);

        User user =
                userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("找不到使用者");
        }

        petService.deletePet(
                id,
                user.getId()
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "更新寵物",
            description = "根據 ID 更新寵物資料"
    )
    @PutMapping("/pets/{id}")
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable Long id,
            @Valid @RequestBody PetRequest petRequest,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ","");
        String email = jwtUtil.getEmailFromToken(token);

        User user = userRepository.findByEmail(email);

        return ResponseEntity.ok(
                petService.updatePet(
                        id,
                        petRequest,
                        user.getId()
                )
        );
    }

    @Operation(
            summary = "取得單一寵物",
            description = "根據 ID 查詢寵物資料"
    )
    @GetMapping("/pets/{id}")
    public ResponseEntity<PetResponse> getPetById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token =
                authHeader.replace("Bearer ","");

        String email =
                jwtUtil.getEmailFromToken(token);

        User user =
                userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("找不到使用者");
        }
        return ResponseEntity.ok(
                petService.getPetById(
                        id,
                        user.getId()
                )
        );
    }
    // 動態搜尋寵物
    // 可依照名字、年齡或名字+年齡搜尋
    @GetMapping("/pets/search")
    public ResponseEntity<List<PetResponse>> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age
    ) {

        return ResponseEntity.ok(
                petService.searchPets(
                        name,
                        age
                )
        );
    }

}
