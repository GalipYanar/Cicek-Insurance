package com.example.insurance.repository;

import com.example.insurance.enums.CampaignCategory;
import com.example.insurance.enums.CampaignStatus;
import com.example.insurance.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Campaign findByCampaignHeaderAndCampaignDescriptionAndCampaignCategory(String campaignHeader,
                                                                           String campaignDescription,
                                                                           CampaignCategory campaignCategory);
    long countByCampaignStatus(CampaignStatus status);
}
