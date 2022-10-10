package hs.saga.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CustomerEligibilityAssessmentEntity extends WorkStepAbstract {

    @Column
    private String customerGrade;


}
