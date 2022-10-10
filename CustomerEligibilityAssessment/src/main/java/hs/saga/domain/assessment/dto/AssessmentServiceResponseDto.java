package hs.saga.domain.assessment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssessmentServiceResponseDto {

    private boolean status;
    private String message;
    private String customerGrade;
}
