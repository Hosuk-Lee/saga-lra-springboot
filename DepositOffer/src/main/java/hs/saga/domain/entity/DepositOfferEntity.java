package hs.saga.domain.entity;

import hs.saga.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity // 테이블명과 칼럼명을 동일하게 셋팅
public class DepositOfferEntity extends BaseEntity {

    @Column
    String accountNumber;

    @Column
    String customerId;

    @Column
    String productCode;

    @Column
    String openDate;

    @Column
    String closeDate;

    @Embedded
    CustomerEligibilityAssessmentEntity customerEligibilityAssessment;

}
