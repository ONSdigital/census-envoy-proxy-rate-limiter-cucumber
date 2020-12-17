package uk.gov.ons.ctp.integration.envoycuc.mockclient;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientFulfilmentRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientWebformRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

public class MockClient implements TestClient {

  private static final Logger log = LoggerFactory.getLogger(MockClient.class);

  private final MockLimiter mockLimiter;

  public MockClient(MockLimiter mockLimiter) {
    this.mockLimiter = mockLimiter;
  }

  @Override
  public void checkFulfilmentRateLimit(
      RateLimiterClient.Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {

    final RateLimiterClientFulfilmentRequest rateLimiterClientFulfilmentRequest =
        new RateLimiterClientFulfilmentRequest(domain, product, caseType, ipAddress, uprn, telNo);
    final RequestValidationStatus requestValidationStatus =
        mockLimiter.postFulfilmentRequest(rateLimiterClientFulfilmentRequest);

    checkValidity(requestValidationStatus);
  }

  @Override
  public void checkWebformRateLimit(Domain domain, String ipAddress)
      throws CTPException, ResponseStatusException {
    RateLimiterClientWebformRequest rateLimiterClientWebformRequest =
        new RateLimiterClientWebformRequest(domain, ipAddress);

    final RequestValidationStatus requestValidationStatus =
        mockLimiter.postWebformRequest(rateLimiterClientWebformRequest);

    checkValidity(requestValidationStatus);
  }

  private void checkValidity(final RequestValidationStatus requestValidationStatus) {
    if (!requestValidationStatus.isValid()) { // invalid request - throw a 429
      final StringBuilder reason = new StringBuilder("Too Many Requests - ");
      requestValidationStatus
          .getLimitStatusList()
          .forEach(stat -> reason.append(stat.toString()).append(" - "));
      throw new ResponseStatusException(
          org.springframework.http.HttpStatus.TOO_MANY_REQUESTS, reason.toString());
    }
  }

  @Override
  public void rollOverTheHour() {
    mockLimiter.resetLimiterMaps();
  }
}
