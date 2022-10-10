package hs.saga.domain.assessment.service;

import hs.saga.config.exception.BusinessException;
import hs.saga.domain.assessment.command.AssessmentFactoryCommand;
import hs.saga.domain.assessment.command.AssessmentInstanceCommand;
import hs.saga.domain.assessment.dto.AssessmentServiceRequestDto;
import hs.saga.domain.assessment.dto.AssessmentServiceResponseDto;
import hs.saga.domain.assessment.entity.CustomerEligibilityAssessment;
import hs.saga.domain.assessment.repo.CustomerEligibilityAssessmentRepository;
import hs.saga.integration.c1.schema.CustomerSchema;
import hs.saga.integration.c1.service.CustomerIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AssessmentService {

    @Autowired
    CustomerEligibilityAssessmentRepository repo;

    @Autowired
    CustomerIntegration customerIntegration;

    @Autowired
    AssessmentFactoryCommand assessmentFactoryCommand;

    //@Transactional
    public AssessmentServiceResponseDto call(AssessmentServiceRequestDto assessmentServiceRequestDto) {

        CustomerEligibilityAssessment entity = repo.findByCustomerId(assessmentServiceRequestDto.getCustomerId())
                .orElseGet( ()-> assessmentFactoryCommand.saveCommand(
                                assessmentServiceRequestDto.getCustomerId(),
                                "open",
                                assessmentServiceRequestDto.getProductCode()
                        )
                );

        CustomerSchema customerSchema = customerIntegration.call(assessmentServiceRequestDto.getCustomerId());

        log.info("{}",customerSchema);
        if ( "premium".equals(customerSchema.getPrimeCode()) ) {
            throw new BusinessException(500, "고객 우대등급 Gold 부터 신규 가능합니다.");
        }




        return AssessmentServiceResponseDto.builder()
                .status(customerSchema.isStatus())
                .message("ok")
                .customerGrade(customerSchema.getPrimeCode())
                .build();
    }

}
