package hs.saga.domain.assessment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AssessmentServiceRequestDto {

    private String customerId;
    private String productCode;
}
