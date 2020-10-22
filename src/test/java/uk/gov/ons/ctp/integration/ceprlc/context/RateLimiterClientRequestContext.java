package uk.gov.ons.ctp.integration.ceprlc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import uk.gov.ons.ctp.integration.ceprlc.client.RateLimiterClientRequest;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class RateLimiterClientRequestContext {

  @Value("${mock-client}")
  private Boolean useStubClient;

  @Value("${envoy.host}")
  private String envoyHost;

  @Value("${envoy.port}")
  private String envoyPort;

  @Value("${envoy.scheme}")
  private String envoyScheme;

  private List<RateLimiterClientRequest> RateLimiterRequestList = new ArrayList<>();
  private int noRequests = 0;
  private List<Boolean> passFail = new ArrayList<>();
  private RateLimiterClientRequest rateLimiterClientRequest = new RateLimiterClientRequest();
}
