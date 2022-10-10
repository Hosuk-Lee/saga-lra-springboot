package hs.saga.domain.assessment.repo;

import hs.saga.domain.assessment.entity.CustomerEligibilityAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerEligibilityAssessmentRepository extends JpaRepository<CustomerEligibilityAssessment,Long> {

    Optional<CustomerEligibilityAssessment> findByCustomerId(String customerId);
}
