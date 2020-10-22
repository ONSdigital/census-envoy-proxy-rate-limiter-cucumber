package uk.gov.ons.ctp.integration.ceprlc.main;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.integration.ceprlc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.ceprlc.mockclient.MockClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;

@ContextConfiguration(
    classes = {
      SpringIntegrationTest.class,
      RateLimiterClientRequestContext.class,
      RateLimiterClient.class,
      MockClient.class
    },
    loader = SpringBootContextLoader.class,
    initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@Import({RestClient.class})
public class SpringIntegrationTest {

  @Before(order = 0)
  public void init() {}
}
