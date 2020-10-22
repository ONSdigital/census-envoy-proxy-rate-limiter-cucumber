package uk.gov.ons.ctp.integration.ceprlc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class FulfilmentDTOContext {

  @Value("${mock-client}")
  private Boolean useStubClient;

  private List<FulfilmentsRequestWrapperDTO> fulfilmentsList = new ArrayList<>();
  private int noFulfilments = 0;
  private List<Boolean> passFail = new ArrayList<>();

}
