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

  private List<RateLimiterClientRequest> rateLimiterRequestList = new ArrayList<>();
  private int numberRequests;
  private List<Boolean> passList = new ArrayList<>();
  private RateLimiterClientRequest rateLimiterClientRequest = new RateLimiterClientRequest();
  private boolean pending = false;

  public void addPassed(boolean isPass) {
    passList.add(isPass);
  }


}
