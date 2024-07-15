package com.example.insurance.model;


import com.example.insurance.enums.CampaignCategory;
import com.example.insurance.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name="campaign")

public class Campaign {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private Long id;

    @Size(min = 10, max = 50, message = "Kampanya Başlığı 10-50 karakter arası olmalıdır")
    @Pattern(regexp = "^[a-zA-Z0-9].*", message = "Kampanya Başlığı harf yada rakamla başlamalıdır")
    @Column(name = "campaign_header")
    private String campaignHeader;

    @Size(min = 20, max = 200, message = "Kampanya Detayı 20-200 karakter arası olmalıdır")
    @Pattern(regexp = "^[\\p{L}0-9\\p{P}\\p{Zs}]*$", message = "Kampanya Detayında özel karakter kullanabilirsiniz")
    @Column(name = "campaign_description")
    private String campaignDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_category")
    private CampaignCategory campaignCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    private CampaignStatus campaignStatus;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<CampaignHistory> campaignHistories;
}
