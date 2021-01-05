package uk.gov.ons.ctp.integration.envoycuc.config;

import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("blacklist")
public class BlackListConfig {
  private Set<String> ipAddresses;
  private Set<String> telephoneNumbers;
  private Set<String> uprns;
}
