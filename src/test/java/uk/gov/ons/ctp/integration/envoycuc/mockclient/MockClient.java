package uk.gov.ons.ctp.integration.envoycuc.mockclient;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.model.RateLimitResponse;

public class MockClient implements TestClient {

  private static final Logger log = LoggerFactory.getLogger(MockClient.class);

  private final MockLimiter mockLimiter;

  public MockClient(MockLimiter mockLimiter) {
    this.mockLimiter = mockLimiter;
  }

  @Override
  public RateLimitResponse checkRateLimit(
      RateLimiterClient.Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {

    final RateLimiterClientRequest rateLimiterClientRequest =
        new RateLimiterClientRequest(domain, product, caseType, ipAddress, uprn, telNo);
    final RequestValidationStatus requestValidationStatus =
        mockLimiter.postRequest(rateLimiterClientRequest);

    if (!requestValidationStatus.isValid()) { // invalid request - throw a 429
      final StringBuilder reason = new StringBuilder("Too Many Requests - ");
      requestValidationStatus
          .getLimitStatusList()
          .forEach(stat -> reason.append(stat.toString()).append(" - "));
      throw new ResponseStatusException(
          org.springframework.http.HttpStatus.TOO_MANY_REQUESTS, reason.toString());
    }

    RateLimitResponse response = new RateLimitResponse();
    response.setOverallCode("200");
    response.setStatuses(requestValidationStatus.getLimitStatusList());
    final StringBuilder reason = new StringBuilder("Request Successful - ");
    requestValidationStatus
        .getLimitStatusList()
        .forEach(stat -> reason.append(stat.toString()).append(" - "));
    log.debug(reason.toString());
    return response;
  }

  @Override
  public void waitHours(int hours) {
    mockLimiter.waitHours(hours);
  }
}
