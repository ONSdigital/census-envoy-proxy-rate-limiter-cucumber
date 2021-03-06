package uk.gov.ons.ctp.integration.envoycuc.glue;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {
      "src/test/resources/features/limiter-test.feature",
      "src/test/resources/features/roll-forward.feature",
      "src/test/resources/features/combotests/CONTINUATION-POST.feature",
      "src/test/resources/features/combotests/QUESTIONNAIRE-POST-HH.feature",
      "src/test/resources/features/combotests/QUESTIONNAIRE-POST-SPG.feature",
      "src/test/resources/features/combotests/UAC-POST-CE.feature",
      "src/test/resources/features/combotests/UAC-POST-HH.feature",
      "src/test/resources/features/combotests/UAC-POST-SPG.feature",
      "src/test/resources/features/combotests/UAC-SMS-CE.feature",
      "src/test/resources/features/combotests/UAC-SMS-HH.feature",
      "src/test/resources/features/combotests/UAC-SMS-SPG.feature"
    },
    glue = {
      "uk.gov.ons.ctp.integration.envoycuc.steps",
      "uk.gov.ons.ctp.integration.envoycuc.main"
    })
public class RunLimiterTest {}
