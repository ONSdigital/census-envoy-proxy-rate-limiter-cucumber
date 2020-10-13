package uk.gov.ons.ctp.integration.ceprlc.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class CucTestContext {
  @Value("${contact-centre.host}")
  protected String envoyProxyClientBaseUrl;

  @Value("${contact-centre.port}")
  protected String envoyProxyClientBasePort;

  @Value("${contact-centre.username}")
  private String envoyProxyClientUsername;

  @Value("${contact-centre.password}")
  private String envoyProxyClientPassword;

  @Value("${mock-case-service.host}")
  protected String mcsBaseUrl;

  @Value("${mock-case-service.port}")
  protected String mcsBasePort;

  public RestTemplate getRestTemplate() {
    return new RestTemplateBuilder()
        .basicAuthentication(envoyProxyClientUsername, envoyProxyClientPassword)
        .build();
  }
}
