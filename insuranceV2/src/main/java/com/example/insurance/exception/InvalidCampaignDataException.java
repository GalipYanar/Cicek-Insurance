package com.example.insurance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCampaignDataException extends RuntimeException {

    public InvalidCampaignDataException(String message){
        super(message);
    }

}
