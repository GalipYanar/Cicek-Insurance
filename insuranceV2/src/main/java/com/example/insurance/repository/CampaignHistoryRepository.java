package com.example.insurance.repository;

import com.example.insurance.model.CampaignHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CampaignHistoryRepository extends JpaRepository<CampaignHistory, Long> {
}
