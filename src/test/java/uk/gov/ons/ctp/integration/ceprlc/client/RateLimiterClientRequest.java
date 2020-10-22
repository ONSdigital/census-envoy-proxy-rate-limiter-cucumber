package uk.gov.ons.ctp.integration.ceprlc.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

@Data
@NoArgsConstructor
public class RateLimiterClientRequest {

  private RateLimiterClient.Domain domain = Domain.RHSvc;
  private Product product;
  private CaseType caseType;
  private String ipAddress;
  private UniquePropertyReferenceNumber uprn;
  private String telNo;
}
