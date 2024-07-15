package com.example.insurance.exception;

public class InvalidCampaignStatusException extends RuntimeException{

    public InvalidCampaignStatusException(String message) {
        super(message);
    }

}
