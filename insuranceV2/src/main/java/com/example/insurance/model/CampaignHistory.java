package com.example.insurance.model;

import com.example.insurance.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="campaign_history")

public class CampaignHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="campaign_id")
    private Campaign campaign;

    @Enumerated(EnumType.STRING)
    @Column(name="campaign_status")
    private CampaignStatus campaignStatus;

    private LocalDateTime modifiedTime;
}
