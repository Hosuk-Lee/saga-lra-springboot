package hs.saga.integration.c1.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CustomerSchema {

    String customerId;
    String name;
    String primeCode;
    boolean status;

}
