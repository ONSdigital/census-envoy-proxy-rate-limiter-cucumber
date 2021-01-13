package uk.gov.ons.ctp.integration.envoycuc.context;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;

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
   * <p>Note that the prefix must be limited to 3 digits and <= 255, if used for a valid IPv4
   * address octet.
   *
   * <p>dayHour is used where we want values within a test to be constant for THAT CONTEXT -
   * throughout the whole test This is so that we gain a fixed value for the test, but on rerun a
   * new value is created based on DDDHH and will be unique to the live rate limiter and so that
   * tests will run as intended and will not be queried by a constantly running rate limiter.
   */
  @PostConstruct
  private void createUniqueValue() {
    final Date now = new Date(System.currentTimeMillis());
    uniqueValue = now.getTime();
    testValuePrefix = generateValidOctetDigits();
    SimpleDateFormat dddMMFormatter = new SimpleDateFormat("DDDHH");
    dayHour = Integer.parseInt(dddMMFormatter.format(now));
  }

  /**
   * Generate "somewhat" random (within an hour) 3 digits suitable for an IPv4 octet. We divide
   * seconds by 20 to ensure that we have unique values that span an hour.
   *
   * @return "somewhat" random (within an hour) octet of 3 digits
   */
  private String generateValidOctetDigits() {
    int secondsOfDay = LocalDateTime.now().toLocalTime().toSecondOfDay();
    return "" + ((secondsOfDay / 20) % 256);
  }

  public String getUniqueValueAsString() {
    uniqueValue++;
    return uniqueValue.toString();
  }

  public String getUniqueValueAsOctets() {
    long temp = uniqueValue++;
    int[] octet = new int[4];
    for (int i = 0; i < octet.length; i++) {
      octet[i] = (int) (temp % 256);
      temp = temp / 256;
    }
    return octet[3] + "." + octet[2] + "." + octet[1] + "." + octet[0];
  }
}
