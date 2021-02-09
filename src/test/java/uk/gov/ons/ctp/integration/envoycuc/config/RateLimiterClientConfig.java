package uk.gov.ons.ctp.integration.envoycuc.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.rest.RestClientConfig;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimitClient;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.envoycuc.mockclient.MockClient;
import uk.gov.ons.ctp.integration.envoycuc.mockclient.MockLimiter;

@Data
@NoArgsConstructor
@EnableConfigurationProperties
@Configuration
public class RateLimiterClientConfig {

  @Value("${envoy.host}")
  private String envoyHost;

  @Value("${envoy.port}")
  private String envoyPort;

  @Value("${envoy.scheme}")
  private String envoyScheme;

  @Value("${logging.encryption.password}")
  private String encryptionPassword;

  @Value("${mock-client}")
  private Boolean isMockClient;

  private BlackListConfig blacklistConfig;

  @Autowired
  public RateLimiterClientConfig(BlackListConfig blacklistConfig) {
    this.blacklistConfig = blacklistConfig;
  }

  @Bean
  public RateLimitClient rateLimiterClient() throws CTPException {
    int connectionManagerDefaultMaxPerRoute = 20;
    int connectionManagerMaxTotal = 50;

    int connectTimeoutMillis = 0;
    int connectionRequestTimeoutMillis = 0;
    int socketTimeoutMillis = 0;

    RestClientConfig restClientConfig =
        new RestClientConfig(envoyScheme, envoyHost, envoyPort, "", "", connectionManagerDefaultMaxPerRoute,
            connectionManagerMaxTotal,
            connectTimeoutMillis,
            connectionRequestTimeoutMillis,
            socketTimeoutMillis);

    Map<HttpStatus, HttpStatus> httpErrorMapping = new HashMap<>();
    httpErrorMapping.put(HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    final RestClient restClient =
        new RestClient(restClientConfig, httpErrorMapping, HttpStatus.INTERNAL_SERVER_ERROR);
    final Resilience4JCircuitBreakerFactory circuitBreakerFactory =
        new Resilience4JCircuitBreakerFactory();
    final CircuitBreaker circuitBreaker = circuitBreakerFactory.create("envoyLimiterCb");
    return new RateLimitClient(restClient, circuitBreaker, encryptionPassword);
  }

  @Bean
  public TestClient testClient() throws CTPException {
    TestClient client;
    if (isMockClient) {
      client = new MockClient(new MockLimiter(blacklistConfig));
    } else {
      client = rateLimiterClient();
    }
    return client;
  }
}
