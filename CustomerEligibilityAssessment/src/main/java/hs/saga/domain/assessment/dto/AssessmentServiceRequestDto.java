package hs.saga.domain.assessment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssessmentServiceRequestDto {

    private String customerId;
    private String productCode;
}
