package hs.saga.api.deposit.saga.service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class DepositOfferSagaDto {

    private String customerId;
    private String currentDate;
    private String lraId;
}
