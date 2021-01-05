package uk.gov.ons.ctp.integration.envoycuc.context;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class StepsContext {

  private Long uniqueValue;
  private String testValuePrefix;
  private int dayHour;
  private boolean waited;

  /**
   * The following 2 methods are concerned with creating unique values that we can use for tests
   * Which require but do not test either the UPRN, TelNo or IP Address
   *
   * <p>This issue with the live limiter is that once you use a certain value, it gets stored for an
   * hour and goes towards a count To avoid this, I assign a unique value as a prefix to the UPRN,
   * TelNO or IP Address. On @PostConstruct, the unique value is given as Date mmss and is a prefix
   * Each time String getUniqueValueAsString() is called - this value is incremented to give a truly
   * unique value in tests where we don't care about that particular value.
   *
   * <p>Note that the prefix must be limited to 3 digits if used for a valid IPv4 address octet.
   *
   * <p>dayHour is used where we want values within a test to be constant for THAT CONTEXT -
   * throughout the whole test This is so that we gain a fixed value for the test, but on rerun a
   * new value is created based on DDDHH and will be unique to the live rate limiter and so that
   * tests will run as intended and will not be queried by a constantly running rate limiter.
   */
  @PostConstruct
  private void createUniqueValue() {
    final Date now = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter = new SimpleDateFormat("mmss");
    uniqueValue = now.getTime();
    testValuePrefix = StringUtils.chop(formatter.format(now));
    SimpleDateFormat dddMMFormatter = new SimpleDateFormat("DDDHH");
    dayHour = Integer.parseInt(dddMMFormatter.format(now));
  }

  public String getUniqueValueAsString() {
    uniqueValue++;
    return uniqueValue.toString();
  }
}
