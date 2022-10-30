// THIS FILE IS AUTOGENERATED. MODIFICATIONS WILL BE LOST!

package hs.saga.config.rest.integration.custas.service;

import com.kbstar.saga.sagadpoffr.sdk.domain.service.EventProducerService;
import com.kbstar.saga.sagadpoffr.sdk.integration.custas.entity.RetrieveCustChkRsultInput;
import com.kbstar.saga.sagadpoffr.sdk.integration.custas.entity.RetrieveCustChkRsultOutput;
import com.kbstar.saga.sagadpoffr.sdk.integration.facade.IntegrationEntityBuilder;

/**
 * Provide an abstract class for domain custas RetrieveCustChkRsult service.
 *
 * @Generated
 *
 */
public abstract class RetrieveCustChkRsultBase {

  
  protected IntegrationEntityBuilder entityBuilder;
  protected EventProducerService eventProducer;

  public RetrieveCustChkRsultBase(IntegrationEntityBuilder entityBuilder, EventProducerService eventProducer) { 
    this.entityBuilder = entityBuilder;
    this.eventProducer = eventProducer;
    
  }

  
  /**
  * 고객심사
  */
  
  public abstract RetrieveCustChkRsultOutput execute(RetrieveCustChkRsultInput retrieveCustChkRsultInput) ;

    

}