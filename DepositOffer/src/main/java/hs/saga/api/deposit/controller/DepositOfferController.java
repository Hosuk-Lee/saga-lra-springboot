package hs.saga.api.deposit.controller;

import hs.saga.api.deposit.dto.DepositOfferRequestSchema;
import hs.saga.api.deposit.dto.DepositOfferResponseSchema;
import hs.saga.domain.dto.DepositOfferServiceRequestDto;
import hs.saga.domain.dto.DepositOfferServiceResponseDto;
import hs.saga.domain.entity.CustomerEligibilityAssessmentEntity;
import hs.saga.domain.entity.DepositOfferEntity;
import hs.saga.domain.repo.DepositOfferRepository;
import hs.saga.domain.service.DepositOfferService;
import hs.saga.integration.c1.dto.AssessmentRequestSchema;
import hs.saga.integration.c1.dto.AssessmentResponseSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping(value = "/api")
public class DepositOfferController {
    @Autowired
    DepositOfferRepository depositOfferRepository;

    @Autowired
    DepositOfferService depositOfferService;

    @Autowired
    private FluentProducerTemplate template;

    @GetMapping("/deposit-offer/all")
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

    /*
    * 1. Controller ?????? > Camel Template Router ??????
    * 2. ????????? Thread ??? ?????? ??? > ????????? ?????? RequestContextHolder ?????? ??? ???
    *
    * ?????????????????? ???????????? ????????? ???????????
    * [ANSWER] 1) ????????? ???????????? - Spring Security?????? ??????????????? ?????? ????????? ?????? ?????? ????????? ????????? ???.
    * [ANSWER] 2) ???????????? ???????????? - ???????????? ???????????? ???????????? ??????????????? ????????? ???.
    * */
    @PostMapping("/deposit-offer")
    public ResponseEntity<DepositOfferResponseSchema> depositOffer (
            @RequestBody DepositOfferRequestSchema depositOfferRequestSchema
    ){
        DepositOfferServiceRequestDto req = DepositOfferServiceRequestDto.builder()
                .customerId(depositOfferRequestSchema.getCustomerId())
                .productCode(depositOfferRequestSchema.getProductCode())
                .build();

        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("request : {}", servletRequest);
        log.info("Thread Information -> {}", Thread.currentThread());

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // Saga Route ??????
        DepositOfferServiceResponseDto res =
                template.withHeader("customerId", depositOfferRequestSchema.getCustomerId())
                .to("direct:controllerTest")
                .withBody(req)
                .request(DepositOfferServiceResponseDto.class)
        ;
        log.info("Done");

//        DepositOfferServiceResponseDto res = depositOfferService.call(DepositOfferServiceRequestDto.builder()
//                .customerId(depositOfferRequestSchema.getCustomerId())
//                .productCode(depositOfferRequestSchema.getProductCode())
//                .build());
        return ResponseEntity.ok(DepositOfferResponseSchema.builder().id(res.getId()).build());
    }

    @PostMapping("/saga/1/deposit-offer")
    //public ResponseEntity<DepositOfferResponseSchema> saga_1_depositOffer (
    public ResponseEntity<String> saga_depositOffer (
            @RequestBody DepositOfferRequestSchema depositOfferRequestSchema
    ){

        AssessmentRequestSchema ars = AssessmentRequestSchema.builder()
                .customerId(depositOfferRequestSchema.getCustomerId())
                .productCode(depositOfferRequestSchema.getProductCode())
                .build();

        Object obj =
                template.withHeader("customerId", depositOfferRequestSchema.getCustomerId())
                        .to("direct:camel2api")
                        //.request()
                        .withBody(ars)
                        .request(Object.class)
                ;


        log.info("return obj : {}{}" , obj.getClass(),obj);
        return ResponseEntity.ok("Success");

//        DepositOfferServiceResponseDto res = depositOfferService.call(DepositOfferServiceRequestDto.builder()
//                .customerId(depositOfferRequestSchema.getCustomerId())
//                .productCode(depositOfferRequestSchema.getProductCode())
//                .build());

        // return ResponseEntity.ok(DepositOfferResponseSchema.builder().id(res.getId()).build());
    }

    @PostMapping("/saga/2/deposit-offer")
    //public ResponseEntity<DepositOfferResponseSchema> saga_2_depositOffer (
    public ResponseEntity<String> saga_2_depositOffer (
            @RequestBody DepositOfferRequestSchema depositOfferRequestSchema
    ){

        AssessmentRequestSchema ars = AssessmentRequestSchema.builder()
                .customerId(depositOfferRequestSchema.getCustomerId())
                .productCode(depositOfferRequestSchema.getProductCode())
                .build();

        Object obj =
                template.withHeader("customerId", depositOfferRequestSchema.getCustomerId())
                        .to("direct:camel2camel")
                        //.request()
                        .withBody(ars)
                        .request(Object.class)
                ;


        log.info("return obj : {}{}" , obj.getClass(),obj);
        return ResponseEntity.ok("Success");
    }

}
