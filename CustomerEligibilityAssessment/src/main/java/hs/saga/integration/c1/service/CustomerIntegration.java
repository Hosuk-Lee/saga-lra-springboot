package hs.saga.integration.c1.service;

import hs.saga.integration.c1.schema.CustomerSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CustomerIntegration {
    private Map<String,CustomerSchema> custMap;

    @PostConstruct
    void init(){
        custMap = new HashMap<>();

        custMap.put("1000020001",CustomerSchema
                .builder()
                        .customerId("1000020001")
                        .name("고객A")
                        .primeCode("mvp")
                        .status(true)
                .build());
        CustomerSchema customerSchema;
        custMap.put("1000020002",CustomerSchema
                .builder()
                .customerId("1000020002")
                .name("고객B")
                .primeCode("royal")
                .status(true)
                .build());
        custMap.put("1000020003",CustomerSchema
                .builder()
                .customerId("1000020003")
                .name("고객C")
                .primeCode("gold")
                .status(true)
                .build());
        custMap.put("1000020004",CustomerSchema
                .builder()
                .customerId("1000020004")
                .name("고객D")
                .primeCode("premium")
                .status(true)
                .build());
        custMap.put("1000020005",CustomerSchema
                .builder()
                .customerId("1000020005")
                .name("고객E")
                .primeCode("premium")
                .status(false)
                .build());
    }

    public CustomerSchema call(String customerId) {
        log.info("++++{}:{}", "Customer ID ",customerId);
        return custMap.get(customerId);
    }
}
