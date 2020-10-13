package uk.gov.ons.ctp.integration.ceprlc.steps;

import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberSteps {

  @Given("This is a test")
  public void thisIsATest() {
    assertTrue(true);
  }

  @When("I do the test")
  public void iDoTheTest() {
    assertTrue(true);
  }

  @Then("I expect a result")
  public void iExpectAResult() {
    assertTrue(true);
  }
}
