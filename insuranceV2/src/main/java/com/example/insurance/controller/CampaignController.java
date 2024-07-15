package com.example.insurance.controller;

import com.example.insurance.exception.CampaignNotFoundException;
import com.example.insurance.exception.InvalidCampaignStatusException;
import com.example.insurance.model.Campaign;
import com.example.insurance.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kampanya")

public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Operation(summary = "Create a new campaign", description = "Creates a new campaign with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campaign created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Campaign.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/oluştur")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign){
        Campaign createdCampaign = campaignService.createCampaign(
                campaign.getCampaignHeader(),
                campaign.getCampaignDescription(),
                campaign.getCampaignCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/aktif/{id}")
    public ResponseEntity<String> activateCampaignFromPending(@PathVariable("id") Long campaignId) {
        try {
            campaignService.activateCampaignFromPending(campaignId);
            return ResponseEntity.ok("Kampanya aktif edildi.");
        } catch (CampaignNotFoundException | InvalidCampaignStatusException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/deaktif/{id}")
    public ResponseEntity<String> deactivateCampaignFromPending(@PathVariable("id") Long campaignId) {
        try {
            campaignService.deactivateCampaignFromPending(campaignId);
            return ResponseEntity.ok("Kampanya deaktif edildi.");
        } catch (CampaignNotFoundException | InvalidCampaignStatusException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/aktiftodeaktife")
    public ResponseEntity<String> deactivateActiveCampaign(@PathVariable("id") Long campaignId) {
        try {
            campaignService.deactivateActiveCampaign(campaignId);
            return ResponseEntity.ok("Aktif kampanya deaktif edildi.");
        } catch (CampaignNotFoundException | InvalidCampaignStatusException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/totalcampaign")
    public ResponseEntity<Map<String, Long>> getTotalCampaignCount(){
        Map<String, Long> counts = campaignService.getTotalCampaignCount();
        return ResponseEntity.ok(counts);
    }

}
