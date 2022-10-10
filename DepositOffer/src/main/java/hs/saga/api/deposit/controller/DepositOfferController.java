package hs.saga.api.deposit.controller;

import hs.saga.api.deposit.dto.DepositOfferRequestSchema;
import hs.saga.api.deposit.dto.DepositOfferResponseSchema;
import hs.saga.domain.dto.DepositOfferServiceRequestDto;
import hs.saga.domain.dto.DepositOfferServiceResponseDto;
import hs.saga.domain.entity.CustomerEligibilityAssessmentEntity;
import hs.saga.domain.entity.DepositOfferEntity;
import hs.saga.domain.repo.DepositOfferRepository;
import hs.saga.domain.service.DepositOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class DepositOfferController {
    @Autowired
    DepositOfferRepository depositOfferRepository;

    @Autowired
    DepositOfferService depositOfferService;

    @GetMapping("/account/all")
    public ResponseEntity<List<DepositOfferEntity>> depositOfferInquiry (){
        return ResponseEntity.ok(depositOfferRepository.findAll());
    }

    @GetMapping("/account/save")
    public ResponseEntity<DepositOfferEntity> depositOfferSave (){
        DepositOfferEntity account = DepositOfferEntity.builder()
                .customerId("customerId")
                .accountNumber("04390204000001")
                .productCode("04")
                .openDate("20221009")
                .closeDate("")
                .customerEligibilityAssessment(
                        CustomerEligibilityAssessmentEntity.builder().build()
                )
                .build();
        return ResponseEntity.ok(depositOfferRepository.save(account));
    }

    @PostMapping("/deposit-offer")
    public ResponseEntity<DepositOfferResponseSchema> depositOffer (
            @RequestBody DepositOfferRequestSchema depositOfferRequestSchema
    ){
        DepositOfferServiceResponseDto res = depositOfferService.call(DepositOfferServiceRequestDto.builder()
                        .customerId(depositOfferRequestSchema.getCustomerId())
                        .productCode(depositOfferRequestSchema.getProductCode())
                .build());
        return ResponseEntity.ok(DepositOfferResponseSchema.builder().id(res.getId()).build());
    }
}
