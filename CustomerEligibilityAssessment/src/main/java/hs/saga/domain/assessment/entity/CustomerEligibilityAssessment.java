package hs.saga.domain.assessment.entity;

import hs.saga.config.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity // 테이블명과 칼럼명을 동일하게 셋팅
public class CustomerEligibilityAssessment extends BaseEntity {

    @Column
    private String customerId;

    @Column
    private String transactionPurpose;

    @Column
    private String productCode;

    @Column
    private LocalDateTime assessmentAt;

    @Column
    protected String status;
    @Column
    private String message;
    @Column
    private String lraId;

}
