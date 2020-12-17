package uk.gov.ons.ctp.integration.envoycuc.client;

import lombok.Data;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

@Data
public class RateLimiterClientFulfilmentRequest {

  private RateLimiterClient.Domain domain = Domain.RH;
  private Product product;
  private CaseType caseType;
  private String ipAddress;
  private UniquePropertyReferenceNumber uprn;
  private String telNo;

  public RateLimiterClientFulfilmentRequest(
      Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {
    this.domain = domain;
    this.product = product;
    this.caseType = caseType;
    this.ipAddress = ipAddress;
    this.uprn = uprn;
    this.telNo = telNo;
  }

  public RateLimiterClientFulfilmentRequest() {}

  public String toString() {
    return String.format(product.toString(), caseType, ipAddress, uprn, telNo);
  }
}
