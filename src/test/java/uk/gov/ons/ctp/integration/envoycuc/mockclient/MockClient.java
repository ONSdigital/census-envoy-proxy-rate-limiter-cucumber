package uk.gov.ons.ctp.integration.envoycuc.mockclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

public class MockClient implements TestClient {

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

    if (!isValidIpAddress(ipAddress)) {
      ipAddress = null;
    }

    final RequestValidationStatus requestValidationStatus =
        mockLimiter.postFulfilmentRequest(domain, product, caseType, ipAddress, uprn, telNo);

    checkValidity(requestValidationStatus);
  }

  @Override
  public void checkWebformRateLimit(Domain domain, String ipAddress)
      throws CTPException, ResponseStatusException {

    if (!isValidIpAddress(ipAddress)) {
      return;
    }

    final RequestValidationStatus requestValidationStatus =
        mockLimiter.postWebformRequest(domain, ipAddress);

    checkValidity(requestValidationStatus);
  }

  @Override
  public void checkEqLaunchLimit(Domain domain, String ipAddress, int loadSheddingModulus)
      throws CTPException, ResponseStatusException {

    if (!isValidIpAddress(ipAddress)) {
      return;
    }

    // TODO Auto-generated method stub
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

  // use the similar code to the real rate limiter client so that we can ensure the test data
  // will pass validation.
  private boolean isValidIpAddress(String ipAddress) {
    return !StringUtils.isBlank(ipAddress)
        && InetAddressValidator.getInstance().isValidInet4Address(ipAddress);
  }

  @Override
  public void rollOverTheHour() {
    mockLimiter.resetLimiterMaps();
  }
}
