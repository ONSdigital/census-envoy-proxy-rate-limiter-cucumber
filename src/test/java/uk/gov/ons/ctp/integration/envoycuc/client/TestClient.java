package uk.gov.ons.ctp.integration.envoycuc.client;

import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;

public interface TestClient {

  void checkFulfilmentRateLimit(
      Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo)
      throws CTPException, ResponseStatusException;

  void checkWebformRateLimit(Domain domain, String ipAddress)
      throws CTPException, ResponseStatusException;

  void checkEqLaunchLimit(Domain domain, String ipAddress, int loadSheddingModulus)
      throws CTPException, ResponseStatusException;

  void rollOverTheHour();
}
