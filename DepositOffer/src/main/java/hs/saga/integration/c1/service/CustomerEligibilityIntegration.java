package hs.saga.integration.c1.service;

import hs.saga.integration.c1.dto.AssessmentRequestSchema;
import hs.saga.integration.c1.dto.AssessmentResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CustomerEligibilityIntegration {

    @Autowired
    RestTemplate restTemplate;

    public AssessmentResponseSchema call(
            String customerId,
            String productCode
    ){
        // String forObject = restTemplate.getForObject("http://localhost:9102/api/customer-eligibility-assessment", String.class);
        //        URI uri = UriComponentsBuilder
//                .fromUriString("http://localhost:9090")
//                .path("/api/server/user/{userId}/name/{userName}")
//                .encode()
//                .build()
//                .expand(100, "steve") // {userId}, {userName}에 들어갈 값을 순차적으로 입력
//                .toUri();
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9102")
                .path("/api/customer-eligibility-assessment")
                .encode()
                .build()
                .toUri();

        System.out.println(uri);

        // object를 넣어주면 object mapper가 json으로 바꿔주고
        // rest template에서 http body에 json을 넣어줄 것이다.
        AssessmentRequestSchema req = AssessmentRequestSchema.builder()
                .customerId(customerId)
                .productCode(productCode)
                .build();

        //RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AssessmentResponseSchema> response = restTemplate.postForEntity(uri, req, AssessmentResponseSchema.class);
        // uri에 req object를 보내서 응답은 UserResponse.class타입으로 받을 것이다!!
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        return response.getBody();
    }
}
