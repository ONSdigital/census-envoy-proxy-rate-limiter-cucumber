package uk.gov.ons.ctp.integration.envoycuc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientFulfilmentRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientWebformRequest;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class RateLimiterClientRequestContext {

  private List<RateLimiterClientFulfilmentRequest> rateLimiterFulfilmentRequestList =
      new ArrayList<>();
  private List<RateLimiterClientWebformRequest> rateLimiterWebformRequestList = new ArrayList<>();
  private int numberRequests;
  private List<Boolean> passList = new ArrayList<>();
  private boolean pending = false;

  public void addFulfilmentRequest(
      final RateLimiterClientFulfilmentRequest rateLimiterClientFulfilmentRequest) {
    rateLimiterFulfilmentRequestList.add(rateLimiterClientFulfilmentRequest);
  }

  public void addWebformRequest(
      final RateLimiterClientWebformRequest rateLimiterClientWebformRequest) {
    rateLimiterWebformRequestList.add(rateLimiterClientWebformRequest);
  }
}
