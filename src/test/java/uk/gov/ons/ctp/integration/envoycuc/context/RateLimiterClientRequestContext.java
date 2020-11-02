package uk.gov.ons.ctp.integration.envoycuc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class RateLimiterClientRequestContext {

  private List<RateLimiterClientRequest> RateLimiterRequestList = new ArrayList<>();
  private int noRequests = 0;
  private List<Boolean> passFail = new ArrayList<>();
  private RateLimiterClientRequest rateLimiterClientRequest = new RateLimiterClientRequest();
  private boolean pending = false;
}
