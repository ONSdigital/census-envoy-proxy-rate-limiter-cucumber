package uk.gov.ons.ctp.integration.envoycuc.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.envoycuc.config.RateLimiterClientConfig;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.envoycuc.context.StepsContext;

public class LimitTestSteps {

  private static final Logger log = LoggerFactory.getLogger(LimitTestSteps.class);

  private final RateLimiterClientRequestContext rateLimiterClientRequestContext;
  private final RateLimiterClientConfig rateLimiterClientConfig;
  private final TestClient testClient;
  private final StepsContext stepsContext;

  @Autowired
  public LimitTestSteps(
      RateLimiterClientRequestContext rateLimiterClientRequestContext,
      RateLimiterClientConfig rateLimiterClientConfig,
      TestClient testClient,
      StepsContext stepsContext) {
    this.rateLimiterClientRequestContext = rateLimiterClientRequestContext;
    this.rateLimiterClientConfig = rateLimiterClientConfig;
    this.stepsContext = stepsContext;
    this.testClient = testClient;
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} case type {string} individual is {string} uprn {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelCaseTypeIndividualIsUprn(
      final int numberRequest,
      final String productGroup,
      final String deliveryChannel,
      final String caseType,
      final String individualStr,
      final String uprnStr) {

    final String fullUprnStr =
        uprnStr.equals("9999999999999") ? uprnStr : stepsContext.getTestValuePrefix() + uprnStr;

    for (int i = 0; i < numberRequest; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              caseType,
              individualStr,
              fullUprnStr,
              getUniqueValue(),
              getUniqueValue());
      rateLimiterClientRequestContext.addRequest(rateLimiterClientRequest);
    }
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} case type {string} individual is {string} telephone {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelCaseTypeIndividualIsTelephone(
      final int numberRequests,
      final String productGroup,
      final String deliveryChannel,
      final String caseType,
      final String individualStr,
      final String telephone) {

    final String fullTelephone =
        telephone.equals("blacklisted-telNo")
            ? telephone
            : stepsContext.getTestValuePrefix() + telephone;

    for (int i = 0; i < numberRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              caseType,
              individualStr,
              getUniqueValue(),
              fullTelephone,
              getUniqueValue());
      rateLimiterClientRequestContext.addRequest(rateLimiterClientRequest);
    }
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} case type {string} individual is {string} ip {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelCaseTypeIndividualIsIP(
      final int numberRequests,
      final String productGroup,
      final String deliveryChannel,
      final String caseType,
      final String individualStr,
      final String ipAddress) {

    final String fullIpAddress =
        ipAddress.equals("blacklisted-ipAddress")
            ? ipAddress
            : stepsContext.getTestValuePrefix() + ipAddress;

    for (int i = 0; i < numberRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              caseType,
              individualStr,
              getUniqueValue(),
              getUniqueValue(),
              fullIpAddress);
      rateLimiterClientRequestContext.addRequest(rateLimiterClientRequest);
    }
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} case type {string} individual is {string} telephone {string} ipAddress {string} uprn {string}")
  public void
      iHaveFulfilmentRequestsOfProductGroupDeliveryChannelCaseTypeIndividualIsTelephoneIpAddressUprn(
          final int noRequests,
          final String productGroup,
          final String deliveryChannel,
          final String caseType,
          final String individualStr,
          final String telephone,
          final String ipAddress,
          final String uprnStr) {

    final String fullTelephone = stepsContext.getTestValuePrefix() + telephone;
    final String fullIpAddress = stepsContext.getTestValuePrefix() + ipAddress;
    final String fullUprnStr = stepsContext.getTestValuePrefix() + uprnStr;

    for (int i = 0; i < noRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              caseType,
              individualStr,
              fullUprnStr,
              fullTelephone,
              fullIpAddress);

      rateLimiterClientRequestContext.addRequest(rateLimiterClientRequest);
    }
  }

  @When("I post the fulfilments to the envoy proxy client")
  public void iPostTheFulfilmentsToTheEnvoyproxyClient() {

    final List<Boolean> passList = rateLimiterClientRequestContext.getPassList();
    for (RateLimiterClientRequest r : rateLimiterClientRequestContext.getRateLimiterRequestList()) {
      boolean isPass = true;
      try {
        testClient.checkRateLimit(
            r.getDomain(),
            r.getProduct(),
            r.getCaseType(),
            r.getIpAddress(),
            r.getUprn(),
            r.getTelNo());
      } catch (ResponseStatusException ex) {
        HttpStatus httpStatus = ex.getStatus();
        if (httpStatus == HttpStatus.TOO_MANY_REQUESTS) {
          isPass = false;
          log.debug("Rate Limit Exceeded: " + ex.getReason());
        }
      } catch (Exception unexpectedException) {
        throw new RuntimeException(
            "Invalid status thrown for request: " + r.toString(), unexpectedException.getCause());
      }
      passList.add(isPass);
    }
  }

  @Then("I expect the first {int} calls to succeed and {int} calls to fail")
  public void iExpectTheFirstArgCallsToSucceedAndArgCallsToFail(
      int expectedSuccesses, int expectedFailures) {

    if (rateLimiterClientRequestContext.isPending()) {
      throw new PendingException("Tests will not wait more than 15 minutes to run");
    }

    final MutableInt count = new MutableInt();
    final MutableInt actualSuccesses = new MutableInt();
    final MutableInt actualFailures = new MutableInt();

    final int mExpectedSuccesses = expectedSuccesses;
    int passes =
        (int) rateLimiterClientRequestContext.getPassList().stream().filter(s -> s).count();
    int failures = rateLimiterClientRequestContext.getPassList().size() - passes;

    log.info("Passes=" + passes + " Fails=" + failures);
    rateLimiterClientRequestContext
        .getPassList()
        .forEach(
            passfail -> {
              count.increment();
              if (count.getValue() > mExpectedSuccesses) {
                assertFalse("Expects Limiter to be blown", passfail);
                actualFailures.increment();
              } else {
                assertTrue("Expects Fulfilment to be posted successfully", passfail);
                actualSuccesses.increment();
              }
            });

    int actSuccesses = actualSuccesses.getValue();
    assertEquals(
        "Actual No Successes should be " + expectedSuccesses, expectedSuccesses, actSuccesses);
    int actFailures = actualFailures.getValue();
    assertEquals("Actual No Failures should be " + expectedFailures, expectedFailures, actFailures);
  }

  private RateLimiterClientRequest getRateLimiterClientRequest(
      String productGroup,
      String deliveryChannel,
      String caseTypeStr,
      String individualStr,
      String uprnStr,
      String telNo,
      String ipAddress) {
    final RateLimiterClientRequest request = new RateLimiterClientRequest();
    Product.CaseType caseType = Product.CaseType.valueOf(caseTypeStr);
    Product product =
        Product.builder()
            .caseTypes(Collections.singletonList(caseType))
            .productGroup(Product.ProductGroup.valueOf(productGroup))
            .deliveryChannel(Product.DeliveryChannel.valueOf(deliveryChannel))
            .individual(Boolean.valueOf(individualStr))
            .build();
    request.setCaseType(CaseType.valueOf(caseTypeStr));
    request.setProduct(product);
    UniquePropertyReferenceNumber uprn = UniquePropertyReferenceNumber.create(uprnStr);
    request.setUprn(uprn);
    request.setTelNo(telNo);
    request.setIpAddress(ipAddress);
    return request;
  }

  @And("I wait until the hour")
  public void iWaitUntilTheHour() {
    if (!stepsContext.isWaited()) {

      if (rateLimiterClientConfig.getIsMockClient()) {
        testClient.resetLimiterMaps();
        stepsContext.setWaited(true);
        return;
      }

      final Date now = new Date(System.currentTimeMillis());
      SimpleDateFormat formatter = new SimpleDateFormat("mm");
      int nowMinutes = Integer.parseInt(formatter.format(now));
      if (nowMinutes < 45) {
        rateLimiterClientRequestContext.setPending(true);
      } else {
        int timeToWait = 61 - nowMinutes;
        log.info("waiting for " + timeToWait + " minutes.");
        try {
          Thread.sleep(1000 * 60 * timeToWait);
          stepsContext.setWaited(true);
        } catch (InterruptedException ex) {
          final String errorMessage = "Unable to wait any longer, so abandoning the test";
          log.error(errorMessage);
          throw new RuntimeException(errorMessage, ex);
        }
      }
    }
  }

  private String getUniqueValue() {
    return stepsContext.getUniqueValueAsString();
  }
}
