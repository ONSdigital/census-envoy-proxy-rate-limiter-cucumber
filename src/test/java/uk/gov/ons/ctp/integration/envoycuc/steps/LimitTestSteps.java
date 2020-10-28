package uk.gov.ons.ctp.integration.envoycuc.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientProvider;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.envoycuc.mockclient.MockClient;

public class LimitTestSteps {

  private static final Logger log = LoggerFactory.getLogger(LimitTestSteps.class);

  @Autowired private RateLimiterClientRequestContext rateLimiterClientRequestContext;

  @Autowired private MockClient mockClient;

  @Autowired private RateLimiterClientProvider rateLimiterClientprovider;

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} access code {string} individual is {string} uprn {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelAccessCodeIndividualIsUprn(
      final int noRequests,
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr,
      final String uprnStr) {

    for (int i = 0; i < noRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              accessCode,
              individualStr,
              uprnStr,
              getUniqueValue(),
              getUniqueValue());
      rateLimiterClientRequestContext.getRateLimiterRequestList().add(rateLimiterClientRequest);
    }
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} access code {string} individual is {string} telephone {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelAccessCodeIndividualIsTelephone(
      final int noRequests,
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr,
      final String telephone) {

    for (int i = 0; i < noRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              accessCode,
              individualStr,
              getUniqueValue(),
              telephone,
              getUniqueValue());
      rateLimiterClientRequestContext.getRateLimiterRequestList().add(rateLimiterClientRequest);
    }
  }

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} access code {string} individual is {string} ip {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelAccessCodeIndividualIsIP(
      final int noRequests,
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr,
      final String ipAddress) {

    for (int i = 0; i < noRequests; i++) {
      final RateLimiterClientRequest rateLimiterClientRequest =
          getRateLimiterClientRequest(
              productGroup,
              deliveryChannel,
              accessCode,
              individualStr,
              getUniqueValue(),
              getUniqueValue(),
              ipAddress);
      rateLimiterClientRequestContext.getRateLimiterRequestList().add(rateLimiterClientRequest);
    }
  }

  @When("I post the fulfilments to the envoy poxy client")
  public void iPostTheFulfilmentsToTheEnvoyPoxyClient() {
    final List<Boolean> passFailList = rateLimiterClientRequestContext.getPassFail();
    for (RateLimiterClientRequest r : rateLimiterClientRequestContext.getRateLimiterRequestList()) {
      boolean isPass = true;
      if (rateLimiterClientRequestContext.getUseStubClient()) {
        try {
          int status = mockClient.postRequest(r);
          if (status == 200) {
          } else {
            isPass = false;
          }
        } catch (Exception ex) {
          isPass = false;
        }
      } else {
        try {
          rateLimiterClientprovider
              .getRateLimiterClient()
              .checkRateLimit(
                  r.getDomain(),
                  r.getProduct(),
                  r.getCaseType(),
                  r.getIpAddress(),
                  r.getUprn(),
                  r.getTelNo());
        } catch (Exception ex) {
          if (ex.getCause() instanceof HttpClientErrorException.TooManyRequests) {
            isPass = false;
          } else {
            throw new RuntimeException("Invalid status thrown for request: " + r.toString(), ex);
          }
        }
      }
      passFailList.add(isPass);
    }
  }

  @Then("I expect the first {int} calls to succeed and {int} calls to fail")
  public void iExpectTheFirstArgCallsToSucceedAndArgCallsToFail(
      int expectedSuccesses, int expectedFailures) {

    final MutableInt count = new MutableInt();
    final MutableInt actualSuccesses = new MutableInt();
    final MutableInt actualFailures = new MutableInt();

    if (!rateLimiterClientRequestContext.getUseStubClient()
        && rateLimiterClientRequestContext.getHoursSetForward() > 0) {
      // we can't set the real limiter back at the moment, so if we have set the clock forward then
      // we have to expect all failures
      expectedFailures += expectedSuccesses;
      expectedSuccesses = 0;
    }

    final int mExpectedSuccesses = expectedSuccesses;
    rateLimiterClientRequestContext
        .getPassFail()
        .forEach(
            passfail -> {
              count.increment();
              if (count.getValue() > mExpectedSuccesses) {
                assertFalse(passfail);
                actualFailures.increment();
              } else {
                assertTrue(passfail);
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
      String accessCode,
      String individualStr,
      String uprnStr,
      String telNo,
      String ipAddress) {
    final RateLimiterClientRequest request = new RateLimiterClientRequest();
    Product.CaseType caseType = Product.CaseType.valueOf(accessCode);
    Product product =
        Product.builder()
            .caseTypes(Collections.singletonList(caseType))
            .productGroup(Product.ProductGroup.valueOf(productGroup))
            .deliveryChannel(Product.DeliveryChannel.valueOf(deliveryChannel))
            .individual(Boolean.valueOf(individualStr))
            .build();
    request.setCaseType(CaseType.valueOf(accessCode));
    request.setProduct(product);
    UniquePropertyReferenceNumber uprn = UniquePropertyReferenceNumber.create(uprnStr);
    request.setUprn(uprn);
    request.setTelNo(telNo);
    request.setIpAddress(ipAddress);
    return request;
  }

  @And("I wait an hour")
  public void iWaitAnHour() {
    if (rateLimiterClientRequestContext.getUseStubClient()) {
      mockClient.waitHours(1);
    }
  }

  private String getUniqueValue() {
    return rateLimiterClientRequestContext.getUniqueValueAsString();
  }
}
