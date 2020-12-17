package uk.gov.ons.ctp.integration.envoycuc.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

@EqualsAndHashCode(callSuper = true)
@Data
public class RateLimiterClientWebformRequest extends RateLimiterClientRequest {

  public RateLimiterClientWebformRequest(Domain domain, String ipAddress) {
    this.domain = domain;
    this.ipAddress = ipAddress;
  }

  public RateLimiterClientWebformRequest() {}

  public String toString() {
    return ipAddress;
  }
}
