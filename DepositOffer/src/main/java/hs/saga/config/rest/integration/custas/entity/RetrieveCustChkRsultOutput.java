// THIS FILE IS AUTOGENERATED. MODIFICATIONS WILL BE LOST!

package hs.saga.config.rest.integration.custas.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kbstar.saga.sagadpoffr.sdk.domain.entity.Entity;

/**
 * Provide an interface for RetrieveCustChkRsultOutput root entity.
 * 
 *
 * @Generated
 *
 */
@JsonDeserialize(as = RetrieveCustChkRsultOutputEntity.class)

public  interface RetrieveCustChkRsultOutput extends Entity {
  
  String getStatus();
  void setStatus(String status);
  String getMessage();
  void setMessage(String message);
  String getGrade();
  void setGrade(String grade);
}