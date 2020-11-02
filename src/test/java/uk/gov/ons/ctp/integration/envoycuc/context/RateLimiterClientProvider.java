package uk.gov.ons.ctp.integration.envoycuc.context;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimitClient;

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

  private RateLimitClient rateLimiterClient;
  private boolean waited = false;

  @Value("${mock-client}")
  private Boolean useStubClient;

  private Long uniqueValue;
  private String testValuePrefix;
  private int dayHour;

  @PostConstruct
  public void setupRateLimiterClient() {
    RestClientConfig restClientConfig =
        new RestClientConfig(envoyScheme, envoyHost, envoyPort, "", "");
    Map<HttpStatus, HttpStatus> httpErrorMapping = new HashMap<>();
    httpErrorMapping.put(HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS);
    final RestClient restClient =
        new RestClient(restClientConfig, httpErrorMapping, HttpStatus.INTERNAL_SERVER_ERROR);
    rateLimiterClient = new RateLimitClient(restClient);
  }

  @PostConstruct
  private void createUniqueValue() {
    final Date now = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter = new SimpleDateFormat("mmss");
    uniqueValue = now.getTime();
    testValuePrefix = formatter.format(now);

    SimpleDateFormat dmformatter = new SimpleDateFormat("DDDHH");
    dayHour = Integer.parseInt(dmformatter.format(now));
  }

  public String getUniqueValueAsString() {
    uniqueValue++;
    return uniqueValue.toString();
  }
}
