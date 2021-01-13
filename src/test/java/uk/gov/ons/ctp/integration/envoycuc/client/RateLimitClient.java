package uk.gov.ons.ctp.integration.envoycuc.client;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;

public class RateLimitClient extends RateLimiterClient implements TestClient {

  public RateLimitClient(
      RestClient restClient, CircuitBreaker circuitBreaker, String encryptionPassword) {
    super(restClient, circuitBreaker, encryptionPassword);
  }

  public void rollOverTheHour() {}
}
