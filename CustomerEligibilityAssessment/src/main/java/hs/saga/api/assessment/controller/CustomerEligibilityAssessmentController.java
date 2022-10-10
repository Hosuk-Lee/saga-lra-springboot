package hs.saga.api.assessment.controller;

import hs.saga.api.assessment.dto.AssessmentRequestSchema;
import hs.saga.api.assessment.dto.AssessmentResponseSchema;
import hs.saga.domain.assessment.dto.AssessmentServiceRequestDto;
import hs.saga.domain.assessment.dto.AssessmentServiceResponseDto;
import hs.saga.domain.assessment.entity.CustomerEligibilityAssessment;
import hs.saga.domain.assessment.repo.CustomerEligibilityAssessmentRepository;
import hs.saga.domain.assessment.service.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CustomerEligibilityAssessmentController {

    Logger log = LoggerFactory.getLogger(CustomerEligibilityAssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    CustomerEligibilityAssessmentRepository repo;

    @GetMapping("/inquiry")
    public List<CustomerEligibilityAssessment> inquiry (){
        return repo.findAll();
    }

    @PostMapping("/customer-eligibility-assessment")
    public ResponseEntity<AssessmentResponseSchema> customerEligibilityAssessment (
            @RequestBody AssessmentRequestSchema schema
            ){

        log.info("@@{}", schema);
        AssessmentServiceRequestDto assessmentServiceRequestDto
                = AssessmentServiceRequestDto.builder()
                .customerId(schema.getCustomerId())
                .productCode(schema.getProductCode())
                .build();

        AssessmentServiceResponseDto res = assessmentService.call(assessmentServiceRequestDto);

        return ResponseEntity.ok(AssessmentResponseSchema.builder()
                .status(res.isStatus())
                .message(res.getMessage())
                .customerGrade(res.getCustomerGrade())
                .build());
    }

}
