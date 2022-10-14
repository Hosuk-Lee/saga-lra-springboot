package hs.saga.api.assessment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class AssessmentResponseSchema {

    private boolean status;
    private String message;
    private String customerGrade;
}
