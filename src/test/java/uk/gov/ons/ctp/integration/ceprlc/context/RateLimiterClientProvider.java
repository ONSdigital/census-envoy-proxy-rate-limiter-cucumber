package uk.gov.ons.ctp.integration.ceprlc.context;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.rest.RestClientConfig;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;

@Data
@NoArgsConstructor
@Component
public class RateLimiterClientProvider {

  @Value("${envoy.host}")
  private String envoyHost;

  @Value("${envoy.port}")
  private String envoyPort;

  @Value("${envoy.scheme}")
  private String envoyScheme;

  private RateLimiterClient rateLimiterClient;

  @PostConstruct
  public void setupRateLimiterClient() {
    RestClientConfig restClientConfig =
        new RestClientConfig(envoyScheme, envoyHost, envoyPort, "", "");
    Map<HttpStatus, HttpStatus> httpErrorMapping = new HashMap<>();
    httpErrorMapping.put(HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    final RestClient restClient =
        new RestClient(restClientConfig, httpErrorMapping, HttpStatus.INTERNAL_SERVER_ERROR);
    rateLimiterClient = new RateLimiterClient(restClient);
  }
}
