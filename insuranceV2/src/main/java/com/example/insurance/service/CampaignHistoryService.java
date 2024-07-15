package com.example.insurance.service;

import com.example.insurance.model.CampaignHistory;
import com.example.insurance.repository.CampaignHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class CampaignHistoryService {

    @Autowired
    private CampaignHistoryRepository campaignHistoryRepository;

    @Transactional
    public List<CampaignHistory> getAllCampaignHistory(){
        return campaignHistoryRepository.findAll();
    }
}
