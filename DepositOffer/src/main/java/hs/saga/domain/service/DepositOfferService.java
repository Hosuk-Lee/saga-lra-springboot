package hs.saga.domain.service;

import hs.saga.domain.dto.DepositOfferServiceRequestDto;
import hs.saga.domain.dto.DepositOfferServiceResponseDto;
import hs.saga.domain.entity.CustomerEligibilityAssessmentEntity;
import hs.saga.domain.entity.DepositOfferEntity;
import hs.saga.domain.repo.DepositOfferRepository;
import hs.saga.integration.c1.dto.AssessmentRequestSchema;
import hs.saga.integration.c1.dto.AssessmentResponseSchema;
import hs.saga.integration.c1.service.CustomerEligibilityIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class DepositOfferService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DepositOfferRepository depositOfferRepository;

    @Autowired
    CustomerEligibilityIntegration customerEligibilityIntegration;

    @Transactional
    public DepositOfferServiceResponseDto call(DepositOfferServiceRequestDto depositOfferServiceRequestDto){

        DepositOfferEntity entity = depositOfferRepository.findByCustomerId(depositOfferServiceRequestDto.getCustomerId())
                .orElseGet(() -> {
                            DepositOfferEntity account = DepositOfferEntity.builder()
                                    .customerId(depositOfferServiceRequestDto.getCustomerId())
                                    .accountNumber("")
                                    .productCode("04")
                                    .openDate("20221009")
                                    .closeDate("")
                                    .customerEligibilityAssessment(
                                            CustomerEligibilityAssessmentEntity.builder().build()
                                    )
                                    .build();
                            return depositOfferRepository.save(account);
                        }
                );


        AssessmentResponseSchema assessmentResponseSchema = customerEligibilityIntegration.call(
                entity.getCustomerId(),
                entity.getProductCode()
        );

        entity.getCustomerEligibilityAssessment().setCustomerGrade(assessmentResponseSchema.getCustomerGrade());
        entity.getCustomerEligibilityAssessment().setStatus(assessmentResponseSchema.isStatus());
        entity.getCustomerEligibilityAssessment().setMessage(assessmentResponseSchema.getMessage());

        return DepositOfferServiceResponseDto.builder().id(String.valueOf(entity.getId())).build();
    }
}
