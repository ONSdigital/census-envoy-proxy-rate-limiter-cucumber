package uk.gov.ons.ctp.integration.envoycuc.main;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientProvider;
import uk.gov.ons.ctp.integration.envoycuc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.envoycuc.mockclient.MockClient;

@ContextConfiguration(
    classes = {
      SpringIntegrationTest.class,
      RateLimiterClientRequestContext.class,
      MockClient.class,
      RateLimiterClientProvider.class
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
