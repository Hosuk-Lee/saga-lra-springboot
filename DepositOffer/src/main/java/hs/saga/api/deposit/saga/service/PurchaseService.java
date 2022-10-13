package hs.saga.api.deposit.saga.service;

import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    static final Logger LOG = LoggerFactory.getLogger(PurchaseService.class);

    public void generateError(@Header("quantity") Integer quantity) {

        if(quantity == 27) {

            LOG.info("Throwing exception for testing purposes...");
            throw new IllegalStateException("Forced exception when quantity equals 27 for testing purposes");
        }


    }

}
