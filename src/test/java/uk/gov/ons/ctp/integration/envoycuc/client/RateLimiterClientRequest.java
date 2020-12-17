package uk.gov.ons.ctp.integration.envoycuc.client;

import lombok.Data;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

@Data
public abstract class RateLimiterClientRequest {
  protected RateLimiterClient.Domain domain = Domain.RH;
  protected Product product;
  protected CaseType caseType;
  protected String ipAddress;
  protected UniquePropertyReferenceNumber uprn;
  protected String telNo;
}
