package hs.saga.api.assessment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AssessmentRequestSchema {

    private String customerId;
    private String productCode;
}
