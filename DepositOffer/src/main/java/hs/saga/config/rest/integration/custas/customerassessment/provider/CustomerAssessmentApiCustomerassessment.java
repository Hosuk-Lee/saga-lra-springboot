// THIS FILE IS AUTOGENERATED. MODIFICATIONS WILL BE LOST!

package hs.saga.config.rest.integration.custas.customerassessment.provider;

import com.kbstar.saga.sagadpoffr.sdk.integration.custas.customerassessment.model.CustomerAssessmentRequest;
import com.kbstar.saga.sagadpoffr.sdk.integration.custas.customerassessment.model.CustomerAssessmentResponse;
import com.kbstar.saga.sagadpoffr.sdk.integration.util.RequestUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider class to wrap call for a remote api operation(s) for path /customer-assessment
 * @Generated
 */
@Service
@ComponentScan(basePackages = "com.kbstar.saga.sagadpoffr.sdk.integration.custas.customerassessment.provider")
public class CustomerAssessmentApiCustomerassessment {

  private final RequestUtil requestUtil;

  public CustomerAssessmentApiCustomerassessment(RequestUtil requestUtil) {
    this.requestUtil = requestUtil;
  }

  public ResponseEntity<CustomerAssessmentResponse> postCustomerAssessment(CustomerAssessmentRequest customerAssessmentRequest, HttpHeaders headerParams) {
    //check if header params already exists
    if(headerParams == null) {
      headerParams = new HttpHeaders();
    }

    // Prepare params
    Map<String, Object> pathParams = new HashMap<String, Object>();
    Map<String, Object> queryParams = new HashMap<String, Object>();
    Object _bodyObj = null;
    _bodyObj = customerAssessmentRequest;

    return requestUtil.requestForOne("custas-customerassessment", "/customer-assessment", "POST", CustomerAssessmentResponse.class, _bodyObj, pathParams, queryParams, headerParams);
    
  }

}
