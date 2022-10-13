package hs.saga.api.assessment.controller;

import hs.saga.api.assessment.dto.AssessmentRequestSchema;
import hs.saga.api.assessment.dto.AssessmentResponseSchema;
import hs.saga.config.exception.BusinessException;
import hs.saga.domain.assessment.dto.AssessmentServiceRequestDto;
import hs.saga.domain.assessment.dto.AssessmentServiceResponseDto;
import hs.saga.domain.assessment.entity.CustomerEligibilityAssessment;
import hs.saga.domain.assessment.repo.CustomerEligibilityAssessmentRepository;
import hs.saga.domain.assessment.service.AssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.processor.DefaultErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CustomerEligibilityAssessmentController {

    Logger log = LoggerFactory.getLogger(CustomerEligibilityAssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    CustomerEligibilityAssessmentRepository repo;

    @Autowired
    private FluentProducerTemplate template;

    @GetMapping("/customer-eligibility-assessment/inquiry")
    public List<CustomerEligibilityAssessment> inquiry (){
        return repo.findAll();
    }

    @PostMapping("/customer-eligibility-assessment")
    public ResponseEntity<AssessmentResponseSchema> customerEligibilityAssessment (
            @RequestBody AssessmentRequestSchema schema
    ){
        log.info("Register Saga LRA");
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("####"+ servletRequest);
        Enumeration<String> hNames = servletRequest.getHeaderNames();

        while (hNames.hasMoreElements()) {
            String s = hNames.nextElement();
            String header = servletRequest.getHeader(s);

            log.info("@@{}:{}", s,header);
        }


        log.info("@@{}", schema);
        AssessmentServiceRequestDto req
                = AssessmentServiceRequestDto.builder()
                .customerId(schema.getCustomerId())
                .productCode(schema.getProductCode())
                .build();

        AssessmentServiceResponseDto res = null;
        try {
            res = template.withHeader("customerId", schema.getCustomerId())
                    .withHeader("Long-Running-Action", servletRequest.getHeader("Long-Running-Action"))
                    .to("direct:controllerTest")
                    .withBody(req)
                    .request(AssessmentServiceResponseDto.class);
        }catch (Exception e) {
            log.error("error " , e);
            throw new BusinessException(404,"오류..");
        }


        //AssessmentServiceResponseDto res = assessmentService.call(assessmentServiceRequestDto);

        return ResponseEntity.ok(AssessmentResponseSchema.builder()
                .status(res.isStatus())
                .message(res.getMessage())
                .customerGrade(res.getCustomerGrade())
                .build());
    }

}


