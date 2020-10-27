package uk.gov.ons.ctp.integration.ceprlc.main;

import io.cucumber.java.Before;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.rest.RestClientConfig;
import uk.gov.ons.ctp.integration.ceprlc.context.RateLimiterClientProvider;
import uk.gov.ons.ctp.integration.ceprlc.context.RateLimiterClientRequestContext;
import uk.gov.ons.ctp.integration.ceprlc.mockclient.MockClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;

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
