package hs.saga.domain.assessment.command;

import hs.saga.domain.assessment.entity.CustomerEligibilityAssessment;
import hs.saga.domain.assessment.repo.CustomerEligibilityAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AssessmentFactoryCommand {

    @Autowired
    CustomerEligibilityAssessmentRepository repo;

    public CustomerEligibilityAssessment saveCommand(
            String customerId,
            String transactionPurpose,
            String productCode
    ){
        return repo.save(
                CustomerEligibilityAssessment.builder()
                        .customerId(customerId)
                        .transactionPurpose(transactionPurpose)
                        .productCode(productCode)
                        .assessmentAt(LocalDateTime.now())
                        .status("Active")
                        .message("")
                        .build()
        );

    }
}
