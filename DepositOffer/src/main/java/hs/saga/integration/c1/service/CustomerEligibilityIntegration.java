package hs.saga.integration.c1.service;

import hs.saga.config.camel.CamelThreadLocal;
import hs.saga.config.exception.BusinessException;
import hs.saga.config.rest.integration.util.RequestUtil;
import hs.saga.integration.c1.dto.AssessmentRequestSchema;
import hs.saga.integration.c1.dto.AssessmentResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerEligibilityIntegration {

    @Autowired
    RestTemplate restTemplate;

    private final RequestUtil requestUtil;

    public CustomerEligibilityIntegration(RequestUtil requestUtil) {
        this.requestUtil = requestUtil;
    }

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
        System.out.println("URI : "+uri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        // headers.add("Authorization", "웹 헤더에 있던 값");
        headers.add("Long-Running-Action", CamelThreadLocal.getLra());

        System.out.println("Headers : " + headers);

        // object를 넣어주면 object mapper가 json으로 바꿔주고
        // rest template에서 http body에 json을 넣어줄 것이다.
        AssessmentRequestSchema req = AssessmentRequestSchema.builder()
                .customerId(customerId)
                .productCode(productCode)
                .build();

        // 기본
//        ResponseEntity<AssessmentResponseSchema> response
//                = restTemplate.postForEntity(uri, req, AssessmentResponseSchema.class);

        // 헤더변경
        HttpEntity httpEntity = new HttpEntity(req,headers);
        ResponseEntity<AssessmentResponseSchema> response
                = restTemplate.postForEntity(uri, httpEntity, AssessmentResponseSchema.class);

        // Response 출력
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        return response.getBody();
    }

    public AssessmentResponseSchema call2(
            String customerId,
            String productCode
    ){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        AssessmentRequestSchema req = AssessmentRequestSchema.builder()
                .customerId(customerId)
                .productCode(productCode)
                .build();

        ResponseEntity<AssessmentResponseSchema> response;
        try{
            // response = customerAssessmentApiCustomerassessment.postCustomerAssessment(request, httpHeaders);
            // Prepare params
            Map<String, Object> pathParams = new HashMap<String, Object>();
            Map<String, Object> queryParams = new HashMap<String, Object>();
            Object _bodyObj = null;
            _bodyObj = req;
            response = requestUtil.requestForOne("customer-assessment", "/api/customer-eligibility-assessment", "POST", AssessmentResponseSchema.class, _bodyObj, pathParams, queryParams, httpHeaders);
        }catch (Exception e){
            throw new BusinessException(500,e.getMessage());
        }

        return response.getBody();
    }
}
