package uk.gov.ons.ctp.integration.envoycuc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class RateLimiterClientRequestContext {

  @Value("${mock-client}")
  private Boolean useStubClient;

  private List<RateLimiterClientRequest> RateLimiterRequestList = new ArrayList<>();
  private int noRequests = 0;
  private List<Boolean> passFail = new ArrayList<>();
  private RateLimiterClientRequest rateLimiterClientRequest = new RateLimiterClientRequest();
  private int hoursSetForward = 0;
  private Long uniqueValue;

  @PostConstruct
  private void createUniqueValue() {
      final Date now = new Date(System.currentTimeMillis());
      uniqueValue = now.getTime();
    }

  public String getUniqueValueAsString() {
    uniqueValue++;
    return uniqueValue.toString();
  }
}
