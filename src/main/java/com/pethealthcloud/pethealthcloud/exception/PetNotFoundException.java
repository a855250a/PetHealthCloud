package com.pethealthcloud.pethealthcloud.exception;

import com.pethealthcloud.pethealthcloud.entity.Pet;

public class PetNotFoundException extends RuntimeException{
    public PetNotFoundException(String message) {
        super(message);
    }
}
