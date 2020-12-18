package uk.gov.ons.ctp.integration.envoycuc.client;

import lombok.Data;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

@Data
public class RateLimiterClientWebformRequest {

  protected RateLimiterClient.Domain domain = Domain.RH;
  protected String ipAddress;

  public RateLimiterClientWebformRequest(Domain domain, String ipAddress) {
    this.domain = domain;
    this.ipAddress = ipAddress;
  }

  public String toString() {
    return ipAddress;
  }
}
