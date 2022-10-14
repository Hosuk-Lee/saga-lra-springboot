package hs.saga.api.deposit.saga.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepositOfferSagaService {

    public void complete(DepositOfferSagaDto param){
        log.info("[OK] Saga Service complete {}", param);
    }

    public void compensate(DepositOfferSagaDto param){
        log.info("[FAIL] Saga Service compensate {}", param);
    }
}
