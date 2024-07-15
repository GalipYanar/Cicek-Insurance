package com.example.insurance.service;

import com.example.insurance.enums.CampaignCategory;
import com.example.insurance.enums.CampaignStatus;
import com.example.insurance.exception.CampaignNotFoundException;
import com.example.insurance.exception.InvalidCampaignDataException;
import com.example.insurance.exception.InvalidCampaignStatusException;
import com.example.insurance.model.Campaign;
import com.example.insurance.model.CampaignHistory;
import com.example.insurance.repository.CampaignHistoryRepository;
import com.example.insurance.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignHistoryRepository campaignHistoryRepository;


    @Transactional
    public Campaign createCampaign(String campaignHeader, String campaignDescription, CampaignCategory campaignCategory){

        Campaign existingCampaign = campaignRepository.findByCampaignHeaderAndCampaignDescriptionAndCampaignCategory(campaignHeader,
                campaignDescription, campaignCategory);

        if (existingCampaign != null){
            Campaign duplicateCampaign = new Campaign();
            duplicateCampaign.setCampaignHeader(campaignHeader);
            duplicateCampaign.setCampaignDescription(campaignDescription);
            duplicateCampaign.setCampaignCategory(campaignCategory);
            duplicateCampaign.setCampaignStatus(CampaignStatus.MUKERRER);

            Campaign savedCampaign = campaignRepository.save(duplicateCampaign);

            CampaignHistory campaignHistory = new CampaignHistory();
            campaignHistory.setCampaign(savedCampaign);
            campaignHistory.setCampaignStatus(savedCampaign.getCampaignStatus());
            campaignHistoryRepository.save(campaignHistory);

            return savedCampaign;
        }

        if (campaignHeader == null || campaignHeader.isEmpty() || campaignDescription == null || campaignHeader.isEmpty()){
            throw new InvalidCampaignDataException("Kampanya başlığı ve açıklaması boş olamaz.");
        }

        if (campaignHeader.length() < 10 || campaignHeader.length() > 50 || campaignDescription.length() < 20 || campaignDescription.length() > 200){
            throw new InvalidCampaignDataException("Kampanya başlığı 10-50 karakter arası, açıklaması 20-200 karakter arası olmalıdır.");
        }

        if (!campaignHeader.matches("^[a-zA-Z0-9].*")){
            throw new InvalidCampaignDataException("Kampanya başlığı harf veya rakamla başlamalıdır..");
        }

        if (!campaignDescription.matches("^[\\p{L}0-9\\p{P}\\p{Zs}]*$")){
            throw new InvalidCampaignDataException("Kampanya açıklamasında özel karakter kullanılabilir.");
        }

        Campaign campaign = new Campaign();
        campaign.setCampaignHeader(campaignHeader);
        campaign.setCampaignDescription(campaignDescription);
        campaign.setCampaignCategory(campaignCategory);

        if (campaignCategory == CampaignCategory.HAYAT_SIGORTASI){
            campaign.setCampaignStatus(CampaignStatus.AKTIF);

        }else {
            campaign.setCampaignStatus(CampaignStatus.ONAY_BEKLIYOR);
        }

        Campaign savedCampaign = campaignRepository.save(campaign);

        //oluşturduğum kampanyayı otomatik olarak kampanya tarihçesine atıyor
        CampaignHistory campaignHistory = new CampaignHistory();
        campaignHistory.setCampaign(savedCampaign);
        campaignHistory.setCampaignStatus(savedCampaign.getCampaignStatus());
        campaignHistoryRepository.save(campaignHistory);

        return savedCampaign;

    }

    @Transactional
    public List<Campaign> getListAllCampaign(){
        return campaignRepository.findAll();
    }

    @Transactional
    public Campaign getCampaignById(Long campaignId){
        return campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Kampanya Bulunamadı. ID = " + campaignId));
    }

    /* bütün statüleri bir method içerisinde değiştirmek istediğimde hata alıyorum. o yüzden hepsini ayrı birer method şeklinde yazdım*/
    @Transactional
    public void activateCampaignFromPending(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Kampanya Bulunamadı. ID = " + campaignId));

        if (campaign.getCampaignStatus() == CampaignStatus.ONAY_BEKLIYOR) {
            campaign.setCampaignStatus(CampaignStatus.AKTIF);
        } else {
            throw new InvalidCampaignStatusException("Kampanya aktif edilemez. Geçerli statü: " + campaign.getCampaignStatus());
        }

        campaignRepository.save(campaign);

        CampaignHistory campaignHistory = new CampaignHistory();
        campaignHistory.setCampaign(campaign);
        campaignHistory.setCampaignStatus(campaign.getCampaignStatus());
        campaignHistory.setModifiedTime(LocalDateTime.now());
        campaignHistoryRepository.save(campaignHistory);
    }

    @Transactional
    public void deactivateCampaignFromPending(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Kampanya Bulunamadı. ID = " + campaignId));

        if (campaign.getCampaignStatus() == CampaignStatus.ONAY_BEKLIYOR) {
            campaign.setCampaignStatus(CampaignStatus.DEAKTIF);
        } else {
            throw new InvalidCampaignStatusException("Kampanya deaktif edilemez. Geçerli statü: " + campaign.getCampaignStatus());
        }

        campaignRepository.save(campaign);

        CampaignHistory campaignHistory = new CampaignHistory();
        campaignHistory.setCampaign(campaign);
        campaignHistory.setCampaignStatus(campaign.getCampaignStatus());
        campaignHistory.setModifiedTime(LocalDateTime.now());
        campaignHistoryRepository.save(campaignHistory);
    }

    @Transactional
    public void deactivateActiveCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Kampanya Bulunamadı. ID = " + campaignId));

        if (campaign.getCampaignStatus() == CampaignStatus.AKTIF) {
            campaign.setCampaignStatus(CampaignStatus.DEAKTIF);
        } else {
            throw new InvalidCampaignStatusException("Sadece aktif kampanyalar deaktif edilebilir. Geçerli statü: " + campaign.getCampaignStatus());
        }

        campaignRepository.save(campaign);

        CampaignHistory campaignHistory = new CampaignHistory();
        campaignHistory.setCampaign(campaign);
        campaignHistory.setCampaignStatus(campaign.getCampaignStatus());
        campaignHistory.setModifiedTime(LocalDateTime.now());
        campaignHistoryRepository.save(campaignHistory);
    }

    @Transactional
    public void deleteCampaignById(Long campaignId){
        if (!campaignRepository.existsById(campaignId)){
            throw new CampaignNotFoundException("Silinecek kampanya bulunamadı.. ID = " + campaignId);
        }
        campaignRepository.deleteById(campaignId);
    }

    @Transactional
    public Map<String, Long> getTotalCampaignCount(){

        Map<String, Long> counts = new HashMap<>();
        long acviteCount = campaignRepository.countByCampaignStatus(CampaignStatus.AKTIF);
        long deaktifCount = campaignRepository.countByCampaignStatus(CampaignStatus.DEAKTIF);
        long duplicateCount = campaignRepository.countByCampaignStatus(CampaignStatus.MUKERRER);
        long waitignCount = campaignRepository.countByCampaignStatus(CampaignStatus.ONAY_BEKLIYOR);

        counts.put("acviteCount", acviteCount);
        counts.put("deaktifCount", deaktifCount);
        counts.put("duplicateCount", duplicateCount);
        counts.put("waitignCount", waitignCount);

        return counts;
    }

}
