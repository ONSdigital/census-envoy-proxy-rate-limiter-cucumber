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
  private boolean waited;

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

  /**
   * The following 2 methods are concerned with creating unique values that we can use for tests
   * Which require but do not test either the UPRN, TelNo or IP Address
   *
   * <p>This issue with the live limiter is that once you use a certain value, it gets stored for an
   * hour and goes towards a count To avoid this, I assign a unique value as a prefix to the UPRN,
   * TelNO or IP Address. On @PostConstruct, the unique value is given as Date mmss and is a prefix
   * Each time String getUniqueValueAsString() is called - this value is incremented to give a truly
   * unique value in tests where we don't care about that particular value
   *
   * <p>dayHour is used where we want values within a test to be constant for THAT CONTEXT -
   * throughout the whole test This is so that we gain a fixed value for the test, but on rerun a
   * new value is created based on DDDHH and will be unique to the live rate limiter and so that
   * tests will run as intended and will not be queered by a constantly running rate limiter.
   */
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
