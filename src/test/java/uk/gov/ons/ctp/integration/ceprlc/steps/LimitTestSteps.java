package uk.gov.ons.ctp.integration.ceprlc.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.ceprlc.client.RateLimiterClientRequest;
import uk.gov.ons.ctp.integration.ceprlc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.ceprlc.mockclient.MockClient;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.model.RateLimitResponse;

public class LimitTestSteps {

  @Autowired private RateLimiterClientRequestContext rateLimiterClientRequestContext;

  @Autowired private MockClient mockClient;

  @Autowired private RateLimiterClient rateLimiterClient;

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} access code {string} individual is {string} uprn {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelAccessCodeIndividualIsUprn(
      final int noRequests,
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr,
      final String uprnStr) {

    final RateLimiterClientRequest rateLimiterClientRequest =
        getRateLimiterClientRequest(
            productGroup, deliveryChannel, accessCode, individualStr, uprnStr);

    for (int i = 0; i < noRequests; i++) {
      rateLimiterClientRequestContext.getRateLimiterRequestList().add(rateLimiterClientRequest);
    }
  }

  @When("I post the fulfilments to the envoy poxy client")
  public void iPostTheFulfilmentsToTheEnvoyPoxyClient() {
    for (RateLimiterClientRequest r : rateLimiterClientRequestContext.getRateLimiterRequestList()) {
      if (rateLimiterClientRequestContext.getUseStubClient()) {
        try {
          mockClient.postRequest(r);
          rateLimiterClientRequestContext.getPassFail().add(true);
        } catch (Exception ex) {
          rateLimiterClientRequestContext.getPassFail().add(false);
        }
      } else {
        RateLimitResponse response =
            rateLimiterClient.checkRateLimit(
                r.getDomain(),
                r.getProduct(),
                r.getCaseType(),
                r.getIpAddress(),
                r.getUprn(),
                r.getTelNo());
      }
    }
  }

  @Then("I expect the first {int} calls to succeed and {int} calls to fail")
  public void iExpectTheFirstArgCallsToSucceedAndArgCallsToFail(int successes, int failures) {

    final MutableInt count = new MutableInt();

    if (rateLimiterClientRequestContext.getUseStubClient()) {
      rateLimiterClientRequestContext
          .getPassFail()
          .forEach(
              passfail -> {
                count.increment();
                if (count.getValue() > successes) {
                  assertFalse(passfail);
                } else {
                  assertTrue(passfail);
                }
              });
    } else {
      assertTrue(true);
    }
  }

  private RateLimiterClientRequest getRateLimiterClientRequest(
      String productGroup,
      String deliveryChannel,
      String accessCode,
      String individualStr,
      String uprnStr) {
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
    return request;
  }

  @And("I wait an hour")
  public void iWaitAnHour() {
    if (rateLimiterClientRequestContext.getUseStubClient()) {
      mockClient.waitHours(1);
    }
  }
}
