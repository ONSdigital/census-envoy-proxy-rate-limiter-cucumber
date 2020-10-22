package uk.gov.ons.ctp.integration.ceprlc.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.integration.ceprlc.context.FulfilmentDTOContext;
import uk.gov.ons.ctp.integration.ceprlc.context.FulfilmentsRequestWrapperDTO;
import uk.gov.ons.ctp.integration.ceprlc.mockclient.MockClient;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.DeliveryChannel;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentsRequestDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.ProductGroup;
import org.apache.commons.lang3.mutable.MutableInt;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LimitTestSteps {

  @Autowired
  private FulfilmentDTOContext fulfilmentDTOContext;

  @Autowired
  private MockClient mockClient;

  @Given(
      "I have {int} fulfilment requests of product group {string} delivery channel {string} access code {string} individual is {string} uprn {string}")
  public void iHaveFulfilmentRequestsOfProductGroupDeliveryChannelAccessCodeIndividualIsUprn(
      final int noFulfilments,
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr,
      final String uprnStr) {

    final FulfilmentsRequestDTO fulfilmentsRequestDTO = getFulfilmentsRequestDTO(productGroup, deliveryChannel, accessCode, individualStr);
    FulfilmentsRequestWrapperDTO f = new FulfilmentsRequestWrapperDTO();
    f.setFulfilmentsRequestDTO(fulfilmentsRequestDTO);
    f.setUprn(uprnStr);


    for (int i=0; i < noFulfilments; i++) {
      fulfilmentDTOContext.getFulfilmentsList().add(f);
    }
  }

  @When("I post the fulfilments to the envoy poxy client")
  public void iPostTheFulfilmentsToTheEnvoyPoxyClient() {
    for (FulfilmentsRequestWrapperDTO f : fulfilmentDTOContext.getFulfilmentsList()) {
      try {
        mockClient.postFulfilment(f);
        fulfilmentDTOContext.getPassFail().add(true);
      }
      catch (Exception ex) {
        fulfilmentDTOContext.getPassFail().add(false);
      }
    }
  }

  @Then("I expect the first {int} calls to succeed and {int} calls to fail")
  public void iExpectTheFirstArgCallsToSucceedAndArgCallsToFail(int successes, int failures) {

    System.out.println(fulfilmentDTOContext.getPassFail());
    final MutableInt count = new MutableInt();
    fulfilmentDTOContext.getPassFail().forEach( passfail -> {
      count.increment();
      if (count.getValue() > successes) {
        assertFalse(passfail);
      }
      else {
        assertTrue(passfail);
      }
    });
  }

  private FulfilmentsRequestDTO getFulfilmentsRequestDTO(
      final String productGroup,
      final String deliveryChannel,
      final String accessCode,
      final String individualStr) {
    return FulfilmentsRequestDTO.builder()
        .caseType(CaseType.valueOf(accessCode))
        .deliveryChannel(DeliveryChannel.valueOf(deliveryChannel))
        .individual(Boolean.valueOf(individualStr))
        .productGroup(ProductGroup.valueOf(productGroup))
        .build();
  }

  @And("I wait an hour")
  public void iWaitAnHour() {
    if (fulfilmentDTOContext.getUseStubClient()) {
      mockClient.waitHours(1);
    }
  }
}
