package com.pethealthcloud.pethealthcloud.service;


import com.pethealthcloud.pethealthcloud.repository.PetRepository;
import com.pethealthcloud.pethealthcloud.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.pethealthcloud.pethealthcloud.entity.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.pethealthcloud.pethealthcloud.dto.PetRequest;
import com.pethealthcloud.pethealthcloud.dto.PetResponse;
import com.pethealthcloud.pethealthcloud.exception.PetNotFoundException;
import com.pethealthcloud.pethealthcloud.exception.InvalidSearchConditionException;
import com.pethealthcloud.pethealthcloud.entity.User;
import com.pethealthcloud.pethealthcloud.exception.ForbiddenException;

import com.pethealthcloud.pethealthcloud.config.JwtUtil;


@Service
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(
            PetRepository petRepository,
            UserRepository userRepository
    ) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }
    // 取得所有寵物資料
    public List<PetResponse> getAllPets(Long userId) {
        List<Pet> pets = petRepository.findByUserId(userId);
        List<PetResponse> responses = new ArrayList<>();

        for (Pet pet : pets) {
            PetResponse response = new PetResponse();

            response.setId(pet.id);
            response.setName(pet.name);
            response.setAge(pet.age);
            response.setWeight(pet.weight);

            responses.add(response);

        }
        return responses;
    }
    // 新增寵物資料
    public PetResponse addPet(
            PetRequest petRequest,
            User user
    ) {
        Pet pet = new Pet();

        pet.name = petRequest.getName();
        pet.age = petRequest.getAge();
        pet.weight = petRequest.getWeight();

        pet.setUser(user);

        Pet savedPet = petRepository.save(pet);
        PetResponse response = new PetResponse();

        response.setId(savedPet.id);
        response.setName(savedPet.name);
        response.setAge(savedPet.age);
        response.setWeight(savedPet.weight);
        return response;
    }
    // 根據 ID 刪除寵物資料
    public void deletePet(
            Long id,
            Long userId
    ) {
        Optional<Pet> optionalPet =
                petRepository.findById(id);

        if (optionalPet.isEmpty()) {
            throw new PetNotFoundException("找不到寵物");
        }

        Pet pet = optionalPet.get();

        if (!pet.getUser().getId().equals(userId)) {
            throw new ForbiddenException("無權限刪除此寵物");
        }

        petRepository.delete(pet);
    }
    // 根據 ID 更新寵物資料
    public PetResponse updatePet(
            Long id,
            PetRequest petRequest,
            Long userId
    ) {
        Optional<Pet> optionalPet =
                petRepository.findById(id);

        if (optionalPet.isEmpty()) {
            throw new PetNotFoundException("找不到寵物");
        }
        Pet pet = optionalPet.get();

        if (!pet.getUser().getId().equals(userId)) {
            throw new ForbiddenException("無權限修改此寵物");
        }

        pet.name = petRequest.getName();
        pet.age = petRequest.getAge();
        pet.weight = petRequest.getWeight();

        Pet savedPet = petRepository.save(pet);
        PetResponse response = new PetResponse();

        response.setId(savedPet.id);
        response.setName(savedPet.name);
        response.setAge(savedPet.age);
        response.setWeight(savedPet.weight);

        return response;

    }
    // 根據 ID 取得單一寵物資料
    public PetResponse getPetById(
            Long id,
            Long userId
    ) {
        Optional<Pet> optionalPet
                = petRepository.findById(id);

        if (optionalPet.isEmpty()) {
            throw new PetNotFoundException("找不到寵物");
        }

        Pet pet = optionalPet.get();
        if (!pet.getUser().getId().equals(userId)) {
            throw new ForbiddenException("無權限查看此寵物");
        }

        PetResponse response = new PetResponse();

        response.setId(pet.id);
        response.setName(pet.name);
        response.setAge(pet.age);
        response.setWeight(pet.weight);
        return response;
    }

    // 動態搜尋寵物
    // 可依照名字、年齡或名字+年齡搜尋
    public List<PetResponse> searchPets(
            String name,
            Integer age
    ){

        List<Pet> pets;

        if(name != null && age != null) {
            pets = petRepository.findByNameContainingIgnoreCaseAndAge(
                    name,
                    age
            );
        }else if (name != null) {
            pets = petRepository.findByNameContainingIgnoreCase(name);

        }else if(age != null) {
            pets = petRepository.findByAge(age);
        }else {
            throw new InvalidSearchConditionException(
                    "請至少輸入一個搜尋條件"
            );
        }

        List<PetResponse> responses =
                new ArrayList<>();

        for (Pet pet : pets) {
            PetResponse response = new PetResponse();
            response.setId(pet.id);
            response.setName(pet.name);
            response.setAge(pet.age);
            response.setWeight(pet.weight);

            responses.add(response);
        }
        return responses;
    }

}
