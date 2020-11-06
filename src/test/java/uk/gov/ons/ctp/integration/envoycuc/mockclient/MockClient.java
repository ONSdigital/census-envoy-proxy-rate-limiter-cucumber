package uk.gov.ons.ctp.integration.envoycuc.mockclient;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.client.RateLimiterClientRequest;
import uk.gov.ons.ctp.integration.envoycuc.client.TestClient;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient;
import uk.gov.ons.ctp.integration.ratelimiter.model.CurrentLimit;
import uk.gov.ons.ctp.integration.ratelimiter.model.LimitStatus;
import uk.gov.ons.ctp.integration.ratelimiter.model.RateLimitResponse;

@Data
@NoArgsConstructor
@Component
public class MockClient implements TestClient {

  private static final Logger log = LoggerFactory.getLogger(MockClient.class);

  private Map<String, Integer> allowanceMap = new HashMap<>();
  private Map<String, Map<String, List<Integer>>> postingsTimeMap = new HashMap<>();

  @PostConstruct
  private void clearMaps() {
    allowanceMap.clear();
    postingsTimeMap.clear();
  }

  public RateLimitResponse checkRateLimit(
      RateLimiterClient.Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {

    final RateLimiterClientRequest rateLimiterClientRequest =
        new RateLimiterClientRequest(domain, product, caseType, ipAddress, uprn, telNo);
    final RequestValidationStatus requestValidationStatus = postRequest(rateLimiterClientRequest);

    if (!requestValidationStatus.isValid()) { // invalid request - throw a 429
      final StringBuilder reason = new StringBuilder("Too Many Requests - ");
      requestValidationStatus
          .getLimitStatusList()
          .forEach(stat -> reason.append(stat.toString()).append(" - "));
      throw new ResponseStatusException(
          org.springframework.http.HttpStatus.TOO_MANY_REQUESTS, reason.toString());
    }

    RateLimitResponse response = new RateLimitResponse();
    response.setOverallCode("200");
    response.setStatuses(requestValidationStatus.getLimitStatusList());
    final StringBuilder reason = new StringBuilder("Request Successful - ");
    requestValidationStatus
        .getLimitStatusList()
        .forEach(stat -> reason.append(stat.toString()).append(" - "));
    log.info(reason.toString());
    return response;
  }

  public RequestValidationStatus postRequest(
      final RateLimiterClientRequest rateLimiterClientRequest) throws ResponseStatusException {

    List<String> requestKeyList = getKeys(rateLimiterClientRequest);
    final RequestValidationStatus requestValidationStatus =
        createRequestValidationStatus(requestKeyList, rateLimiterClientRequest);
    postRequest(
        requestKeyList,
        rateLimiterClientRequest); // always post - it burns allowances every time for all scenarios

    return requestValidationStatus;
  }

  private List<String> getKeys(RateLimiterClientRequest request) {
    List<String> keyList = new ArrayList<>();
    StringBuilder keyBuff =
        new StringBuilder(request.getProduct().getProductGroup().name().toUpperCase())
            .append("-")
            .append(request.getProduct().getIndividual().toString().toUpperCase())
            .append("-")
            .append(request.getProduct().getDeliveryChannel().name().toUpperCase())
            .append("-")
            .append(request.getCaseType().name().toUpperCase())
            .append("-");
    if (request.getUprn() != null && request.getUprn().getValue() != 0) {
      keyList.add(keyBuff.toString() + "UPRN");
    }
    if (request.getIpAddress() != null) {
      keyList.add(keyBuff.toString() + "IP");
    }
    if (request.getTelNo() != null) {
      keyList.add(keyBuff.toString() + "TELNO");
    }
    return keyList;
  }

  private RequestValidationStatus createRequestValidationStatus(
      final List<String> requestKeyList, final RateLimiterClientRequest f) {
    final RequestValidationStatus requestValidationStatus = new RequestValidationStatus();
    for (String requestKey : requestKeyList) {
      if (!allowanceMap.containsKey(requestKey)) {
        continue; // not in scope so is valid....
      }
      final int noRequestAllowed = allowanceMap.get(requestKey);
      Map<String, List<Integer>> postedMap = postingsTimeMap.get(requestKey);
      if (postedMap == null) {
        return requestValidationStatus;
      }
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];

      String valueToRecord = "";
      if (keyType.equals("UPRN")) {
        valueToRecord = f.getUprn().getValue() + "";
      }
      if (keyType.equals("IP")) {
        valueToRecord = f.getIpAddress();
      }
      if (keyType.equals("TELNO")) {
        valueToRecord = f.getTelNo();
      }

      final List<Integer> postingsList =
          postedMap.containsKey(valueToRecord) ? postedMap.get(valueToRecord) : new ArrayList<>();

      final Date now = new Date(System.currentTimeMillis());
      SimpleDateFormat dmformatter = new SimpleDateFormat("DDDHH");
      int dayHourNow = Integer.parseInt(dmformatter.format(now));
      int postsWithinScopeCount = 0;
      for (int postDateHour : postingsList) {
        if (postDateHour == dayHourNow) {
          postsWithinScopeCount++;
        }
      }
      final String recordKey = requestKey + "(" + valueToRecord + ")";
      recordRequest(requestValidationStatus, recordKey, noRequestAllowed, postsWithinScopeCount);
    }
    return requestValidationStatus;
  }

  private void recordRequest(
      final RequestValidationStatus requestValidationStatus,
      final String recordKey,
      final int noRequestAllowed,
      final int postsWithinScopeCount) {
    final LimitStatus limitStatus = new LimitStatus();
    limitStatus.setCode(recordKey);
    final CurrentLimit currentLimit = new CurrentLimit();
    currentLimit.setRequestsPerUnit(noRequestAllowed);
    currentLimit.setUnit("HOUR");
    limitStatus.setCurrentLimit(currentLimit);
    if (postsWithinScopeCount >= noRequestAllowed) {
      requestValidationStatus.setValid(false);
      limitStatus.setLimitRemaining(0);
    } else {
      limitStatus.setLimitRemaining(noRequestAllowed - postsWithinScopeCount);
    }
    requestValidationStatus.getLimitStatusList().add(limitStatus);
  }

  private void postRequest(List<String> requestKeyList, final RateLimiterClientRequest request) {
    for (String requestKey : requestKeyList) {
      Map<String, List<Integer>> postedMap = postingsTimeMap.get(requestKey);
      if (postedMap == null) {
        return;
      }
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];
      String postingsListKey = null;
      if (keyType.equals("UPRN")) {
        postingsListKey = request.getUprn().getValue() + "";
      }
      if (keyType.equals("IP")) {
        postingsListKey = request.getIpAddress();
      }
      if (keyType.equals("TELNO")) {
        postingsListKey = request.getTelNo();
      }
      postedMap.computeIfAbsent(postingsListKey, k -> new ArrayList<>());
      final Date now = new Date(System.currentTimeMillis());
      SimpleDateFormat dmformatter = new SimpleDateFormat("DDDHH");
      int dayHour = Integer.parseInt(dmformatter.format(now));
      postedMap.get(postingsListKey).add(dayHour);
    }
  }

  @PostConstruct
  private void setupAllowances() {
    allowanceMap.put("UAC-FALSE-SMS-HH-UPRN", 5);
    allowanceMap.put("UAC-FALSE-SMS-SPG-UPRN", 5);
    allowanceMap.put("UAC-FALSE-SMS-CE-UPRN", 5);
    allowanceMap.put("UAC-FALSE-SMS-HH-TELNO", 10);
    allowanceMap.put("UAC-FALSE-SMS-SPG-TELNO", 10);
    allowanceMap.put("UAC-FALSE-SMS-CE-TELNO", 5);
    allowanceMap.put("UAC-FALSE-SMS-HH-IP", 100);
    allowanceMap.put("UAC-FALSE-SMS-SPG-IP", 100);
    allowanceMap.put("UAC-FALSE-SMS-CE-IP", 100);
    allowanceMap.put("UAC-TRUE-SMS-HH-UPRN", 10);
    allowanceMap.put("UAC-TRUE-SMS-SPG-UPRN", 10);
    allowanceMap.put("UAC-TRUE-SMS-CE-UPRN", 50);
    allowanceMap.put("UAC-TRUE-SMS-HH-TELNO", 10);
    allowanceMap.put("UAC-TRUE-SMS-SPG-TELNO", 10);
    allowanceMap.put("UAC-TRUE-SMS-CE-TELNO", 50);
    allowanceMap.put("UAC-TRUE-SMS-HH-IP", 100);
    allowanceMap.put("UAC-TRUE-SMS-SPG-IP", 100);
    allowanceMap.put("UAC-TRUE-SMS-CE-IP", 100);
    allowanceMap.put("UAC-FALSE-POST-HH-UPRN", 1);
    allowanceMap.put("UAC-FALSE-POST-SPG-UPRN", 1);
    allowanceMap.put("UAC-FALSE-POST-CE-UPRN", 1);
    allowanceMap.put("UAC-TRUE-POST-HH-UPRN", 5);
    allowanceMap.put("UAC-TRUE-POST-SPG-UPRN", 5);
    allowanceMap.put("UAC-TRUE-POST-CE-UPRN", 50);
    allowanceMap.put("QUESTIONNAIRE-FALSE-POST-HH-UPRN", 1);
    allowanceMap.put("QUESTIONNAIRE-FALSE-POST-SPG-UPRN", 1);
    allowanceMap.put("QUESTIONNAIRE-TRUE-POST-HH-UPRN", 5);
    allowanceMap.put("QUESTIONNAIRE-TRUE-POST-SPG-UPRN", 5);
    allowanceMap.put("QUESTIONNAIRE-TRUE-POST-CE-UPRN", 50);
    allowanceMap.put("CONTINUATION-FALSE-POST-HH-UPRN", 12);
    allowanceMap.put("CONTINUATION-FALSE-POST-SPG-UPRN", 12);
  }

  @PostConstruct
  private void setupTimeMaps() {
    postingsTimeMap.put("UAC-FALSE-SMS-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-CE-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-HH-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-SPG-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-CE-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-HH-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-SPG-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-SMS-CE-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-CE-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-HH-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-SPG-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-CE-TELNO", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-HH-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-SPG-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-SMS-CE-IP", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-POST-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-POST-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-FALSE-POST-CE-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-POST-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-POST-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("UAC-TRUE-POST-CE-UPRN", getNewTimeMap());
    postingsTimeMap.put("QUESTIONNAIRE-FALSE-POST-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("QUESTIONNAIRE-FALSE-POST-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("QUESTIONNAIRE-TRUE-POST-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("QUESTIONNAIRE-TRUE-POST-SPG-UPRN", getNewTimeMap());
    postingsTimeMap.put("QUESTIONNAIRE-TRUE-POST-CE-UPRN", getNewTimeMap());
    postingsTimeMap.put("CONTINUATION-FALSE-POST-HH-UPRN", getNewTimeMap());
    postingsTimeMap.put("CONTINUATION-FALSE-POST-SPG-UPRN", getNewTimeMap());
  }

  private Map<String, List<Integer>> getNewTimeMap() {
    return new HashMap<>();
  }

  public void waitHours(final int hours) {
    postingsTimeMap.forEach(
        (key1, value1) ->
            value1.forEach(
                (key2, value2) -> {
                  final List<Integer> updatedTimeList = new ArrayList<>();
                  value2.forEach(
                      timeValue -> {
                        updatedTimeList.add(
                            timeValue
                                - hours); // we only store the day and hour now, so to roll back
                        // just subtract hours
                      });
                  value1.put(key2, updatedTimeList);
                }));
  }
}
