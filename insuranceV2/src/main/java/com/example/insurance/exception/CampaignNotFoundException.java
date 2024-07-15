package com.example.insurance.exception;

public class CampaignNotFoundException extends RuntimeException{

    public CampaignNotFoundException(String message){
        super(message);
    }

}
