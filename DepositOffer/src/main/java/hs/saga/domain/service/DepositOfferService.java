package hs.saga.domain.service;

import hs.saga.api.deposit.saga.service.DepositOfferSagaDto;
import hs.saga.config.camel.CamelThreadLocal;
import hs.saga.domain.dto.DepositOfferServiceRequestDto;
import hs.saga.domain.dto.DepositOfferServiceResponseDto;
import hs.saga.domain.entity.CustomerEligibilityAssessmentEntity;
import hs.saga.domain.entity.DepositOfferEntity;
import hs.saga.domain.repo.DepositOfferRepository;
import hs.saga.integration.c1.dto.AssessmentResponseSchema;
import hs.saga.integration.c1.service.CustomerEligibilityIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class DepositOfferService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DepositOfferRepository depositOfferRepository;

    @Autowired
    CustomerEligibilityIntegration customerEligibilityIntegration;

    //@Transactional
    public DepositOfferServiceResponseDto call(DepositOfferServiceRequestDto depositOfferServiceRequestDto){

        log.info("deposit offer service - call : {}", depositOfferServiceRequestDto);
        log.info("Thread Information -> {}", Thread.currentThread());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/*
        HttpServletRequest servletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
 */
        log.info("request {}", requestAttributes);
        log.info("thread-local {}", CamelThreadLocal.getLra());

        DepositOfferEntity entity = depositOfferRepository.findByCustomerId(depositOfferServiceRequestDto.getCustomerId())
                .orElseGet(() -> {
                            DepositOfferEntity account = DepositOfferEntity.builder()
                                    .customerId(depositOfferServiceRequestDto.getCustomerId())
                                    .accountNumber("")
                                    .productCode(depositOfferServiceRequestDto.getProductCode())
                                    .openDate( new SimpleDateFormat("yyyyMMdd").format(new Date()))
                                    .closeDate("")
                                    .customerEligibilityAssessment(
                                            CustomerEligibilityAssessmentEntity.builder().build()
                                    )
                                    .build();
                            return depositOfferRepository.save(account);
                        }
                );
        log.info("instance {}", entity);

        AssessmentResponseSchema assessmentResponseSchema = customerEligibilityIntegration.call(
                entity.getCustomerId(),
                entity.getProductCode()
        );

        entity.getCustomerEligibilityAssessment().setCustomerGrade(assessmentResponseSchema.getCustomerGrade());
        entity.getCustomerEligibilityAssessment().setStatus(assessmentResponseSchema.isStatus());
        entity.getCustomerEligibilityAssessment().setMessage(assessmentResponseSchema.getMessage());
        depositOfferRepository.save(entity);

        return DepositOfferServiceResponseDto.builder().id(String.valueOf(entity.getId())).build();
    }

    public void complete(DepositOfferSagaDto param){
        log.info("[OK] Saga Service complete {}", param);

        DepositOfferEntity entity = depositOfferRepository.findByCustomerId(param.getCustomerId()).orElseThrow();
        log.info("instance {}", entity);

        entity.setStatus("Active");
        entity.setMessage("");
        entity.setLraId(param.getLraId());

        depositOfferRepository.save(entity);
        log.info("[FAIL] Saga Service compensate done");
    }

    public void compensate(DepositOfferSagaDto param){
        log.info("[FAIL] Saga Service compensate {}", param);
        DepositOfferEntity entity = depositOfferRepository.findByCustomerId(param.getCustomerId()).orElseThrow();
        log.info("instance {}", entity);

        entity.setStatus("Cancelled");
        entity.setMessage("LRA Fail compensate");
        entity.setLraId(param.getLraId());

        depositOfferRepository.save(entity);
        log.info("[FAIL] Saga Service compensate done");
    }

}
