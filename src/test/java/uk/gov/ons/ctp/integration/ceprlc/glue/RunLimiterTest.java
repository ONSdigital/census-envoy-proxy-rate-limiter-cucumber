package uk.gov.ons.ctp.integration.ceprlc.glue;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {"src/test/resources/features/limiter-test.feature"},
    glue = {"uk.gov.ons.ctp.integration.ceprlc.steps", "uk.gov.ons.ctp.integration.ceprlc.main"})
public class RunLimiterTest {}
