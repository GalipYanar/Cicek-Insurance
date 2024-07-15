package com.example.insurance.controller;

import com.example.insurance.model.CampaignHistory;
import com.example.insurance.service.CampaignHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/tarihçe")
@RestController

public class CampaignHistoryController {

    @Autowired
    private CampaignHistoryService campaignHistoryService;

    @GetMapping
    public ResponseEntity<List<CampaignHistory>> getAllCampaignHistory(){
        List<CampaignHistory> campaignHistories = campaignHistoryService.getAllCampaignHistory();
        return ResponseEntity.ok(campaignHistories);
    }
}
