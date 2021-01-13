package uk.gov.ons.ctp.integration.envoycuc.main;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.ctp.integration.envoycuc.config.BlackListConfig;
import uk.gov.ons.ctp.integration.envoycuc.config.RateLimiterClientConfig;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.envoycuc.context.StepsContext;

@ContextConfiguration(
    classes = {
      SpringIntegrationTest.class,
      RateLimiterClientRequestContext.class,
      RateLimiterClientConfig.class,
      StepsContext.class,
      BlackListConfig.class
    },
    loader = SpringBootContextLoader.class,
    initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@Import({})
public class SpringIntegrationTest {

  @Before(order = 0)
  public void init() {}
}
