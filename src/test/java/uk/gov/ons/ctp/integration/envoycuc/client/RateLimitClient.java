package uk.gov.ons.ctp.integration.envoycuc.client;

import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;

public class RateLimitClient extends RateLimiterClient implements TestClient {

  public RateLimitClient(RestClient restClient) {
    super(restClient);
  }

  public void waitHours(int hours) {}
}
