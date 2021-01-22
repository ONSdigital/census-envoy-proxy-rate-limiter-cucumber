package uk.gov.ons.ctp.integration.envoycuc.mockclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uk.gov.ons.ctp.common.domain.CaseType;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.envoycuc.config.BlackListConfig;
import uk.gov.ons.ctp.integration.ratelimiter.client.RateLimiterClient.Domain;
import uk.gov.ons.ctp.integration.ratelimiter.model.CurrentLimit;
import uk.gov.ons.ctp.integration.ratelimiter.model.LimitStatus;

/**
 * simulates the real envoy limiter - will store details as each test is run, (is NOT recreated for
 * each test). Normally this would be undesirable in a test situation, but in our case it more
 * realistically models the actual envoy limiter, since we won't be resetting it because it changes
 * state for each test run, then care must be taken creating tests that do not use data that has
 * been used before the in-memory data structures will grow big very fast, so care must be taken if
 * a LOT of calls are made
 */
public class MockLimiter {

  private final Map<String, Integer> allowanceMap = new HashMap<>();
  private final Map<String, Map<String, List<Integer>>> postingsTimeMap = new HashMap<>();
  private final Set<UniquePropertyReferenceNumber> blackListedUprns;
  private final Set<String> blackListedIpAddresses;
  private final Set<String> blackListedTelNumbers;

  public MockLimiter(BlackListConfig blackListConfig) {
    blackListedIpAddresses = blackListConfig.getIpAddresses();
    blackListedTelNumbers = blackListConfig.getTelephoneNumbers();
    blackListedUprns =
        blackListConfig.getUprns().stream()
            .map(s -> UniquePropertyReferenceNumber.create(s))
            .collect(Collectors.toSet());
    setupAllowances();
  }

  public RequestValidationStatus postFulfilmentRequest(
      Domain domain,
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {
    List<String> requestKeyList = getFulfilmentKeys(product, caseType, ipAddress, uprn, telNo);
    final RequestValidationStatus requestValidationStatus =
        createRequestValidationStatus(requestKeyList, uprn, ipAddress, telNo);
    postRequest(
        requestKeyList,
        uprn,
        ipAddress,
        telNo); // always post - it burns allowances every time for all scenarios

    return requestValidationStatus;
  }

  public RequestValidationStatus postWebformRequest(Domain domain, String ipAddress) {
    List<String> requestKeyList = getWebformKeys();
    final RequestValidationStatus requestValidationStatus =
        createRequestValidationStatus(requestKeyList, null, ipAddress, null);
    postRequest(
        requestKeyList,
        null,
        ipAddress,
        null); // always post - it burns allowances every time for all scenarios

    return requestValidationStatus;
  }

  private List<String> getFulfilmentKeys(
      Product product,
      CaseType caseType,
      String ipAddress,
      UniquePropertyReferenceNumber uprn,
      String telNo) {
    List<String> keyList = new ArrayList<>();
    if (ipAddress != null) {
      keyList.add(product.getDeliveryChannel().name().toUpperCase() + "-IP");
    }
    StringBuilder keyBuff =
        new StringBuilder()
            .append(product.getDeliveryChannel().name().toUpperCase())
            .append("-")
            .append(product.getProductGroup().name().toUpperCase())
            .append("-")
            .append(product.getIndividual().toString().toUpperCase())
            .append("-")
            .append(caseType.name().toUpperCase())
            .append("-");
    if (uprn != null && uprn.getValue() != 0) {
      keyList.add(keyBuff.toString() + "UPRN");
    }
    if (telNo != null) {
      keyList.add(keyBuff.toString() + "TELNO");
    }
    return keyList;
  }

  private List<String> getWebformKeys() {
    return Collections.singletonList("IP");
  }

  private RequestValidationStatus createRequestValidationStatus(
      final List<String> requestKeyList,
      final UniquePropertyReferenceNumber uprn,
      final String ipAddress,
      final String telNo) {
    final RequestValidationStatus requestValidationStatus = new RequestValidationStatus();
    for (String requestKey : requestKeyList) {
      if (!allowanceMap.containsKey(requestKey)) {
        continue; // not in scope so is valid....
      }

      final Map<String, List<Integer>> postedMap = postingsTimeMap.get(requestKey);
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];

      int numberRequestsAllowed = allowanceMap.get(requestKey);
      boolean isBlackListed = false;

      if (blackListedIpAddresses.contains(ipAddress)
          || blackListedUprns.contains(uprn)
          || blackListedTelNumbers.contains(telNo)) {
        numberRequestsAllowed = 0;
        isBlackListed = true;
        requestValidationStatus.setValid(false);
      }

      if (!isBlackListed && postedMap == null) {
        return requestValidationStatus;
      }

      String keyToRecord = getListKey(uprn, ipAddress, telNo, keyType);
      final List<Integer> postingsList =
          postedMap != null && postedMap.containsKey(keyToRecord)
              ? postedMap.get(keyToRecord)
              : new ArrayList<>();

      int dayHourNow = getDayHourNow();
      int postsWithinScopeCount = 0;
      for (int postDateHour : postingsList) {
        if (postDateHour == dayHourNow) {
          postsWithinScopeCount++;
        }
      }
      final String recordKey = requestKey + "(" + keyToRecord + ")";
      recordRequest(
          requestValidationStatus, recordKey, numberRequestsAllowed, postsWithinScopeCount);
    }
    return requestValidationStatus;
  }

  private void recordRequest(
      final RequestValidationStatus requestValidationStatus,
      final String recordKey,
      final int numberRequestsAllowed,
      final int postsWithinScopeCount) {

    int limitRemaining;
    if (postsWithinScopeCount >= numberRequestsAllowed) {
      requestValidationStatus.setValid(false);
      limitRemaining = 0;
    } else {
      limitRemaining = numberRequestsAllowed - postsWithinScopeCount;
    }

    final CurrentLimit currentLimit = new CurrentLimit(numberRequestsAllowed, "HOUR");
    final LimitStatus limitStatus = new LimitStatus(recordKey, currentLimit, limitRemaining);
    requestValidationStatus.getLimitStatusList().add(limitStatus);
  }

  private void postRequest(
      List<String> requestKeyList,
      final UniquePropertyReferenceNumber uprn,
      final String ipAddress,
      final String telNo) {
    for (String requestKey : requestKeyList) {
      Map<String, List<Integer>> postedMap = postingsTimeMap.get(requestKey);
      if (postedMap == null) {
        return;
      }
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];
      final String listKey = getListKey(uprn, ipAddress, telNo, keyType);

      postedMap.computeIfAbsent(listKey, k -> new ArrayList<>());
      int dayHour = getDayHourNow();
      postedMap.get(listKey).add(dayHour);
    }
  }

  private String getListKey(
      final UniquePropertyReferenceNumber uprn,
      final String ipAddress,
      final String telNo,
      final String keyType) {
    String listKey = null;
    if (keyType.equals("UPRN")) {
      listKey = uprn.toString();
    }
    if (keyType.equals("IP")) {
      listKey = ipAddress;
    }
    if (keyType.equals("TELNO")) {
      listKey = telNo;
    }
    return listKey;
  }

  private void setupAllowances() {
    allowanceMap.put("SMS-UAC-FALSE-HH-UPRN", 5);
    allowanceMap.put("SMS-UAC-FALSE-HH-TELNO", 10);

    allowanceMap.put("SMS-UAC-FALSE-SPG-UPRN", 5);
    allowanceMap.put("SMS-UAC-FALSE-SPG-TELNO", 10);

    allowanceMap.put("SMS-UAC-FALSE-CE-UPRN", 5);
    allowanceMap.put("SMS-UAC-FALSE-CE-TELNO", 5);

    allowanceMap.put("SMS-UAC-TRUE-HH-UPRN", 10);
    allowanceMap.put("SMS-UAC-TRUE-HH-TELNO", 10);

    allowanceMap.put("SMS-UAC-TRUE-SPG-UPRN", 10);
    allowanceMap.put("SMS-UAC-TRUE-SPG-TELNO", 10);

    allowanceMap.put("SMS-UAC-TRUE-CE-UPRN", 50);
    allowanceMap.put("SMS-UAC-TRUE-CE-TELNO", 50);

    allowanceMap.put("SMS-IP", 100);

    allowanceMap.put("POST-UAC-FALSE-HH-UPRN", 1);
    allowanceMap.put("POST-UAC-FALSE-SPG-UPRN", 1);
    allowanceMap.put("POST-UAC-FALSE-CE-UPRN", 1);

    allowanceMap.put("POST-UAC-TRUE-HH-UPRN", 5);
    allowanceMap.put("POST-UAC-TRUE-SPG-UPRN", 5);
    allowanceMap.put("POST-UAC-TRUE-CE-UPRN", 50);

    allowanceMap.put("POST-QUESTIONNAIRE-FALSE-HH-UPRN", 1);
    allowanceMap.put("POST-QUESTIONNAIRE-FALSE-SPG-UPRN", 1);

    allowanceMap.put("POST-QUESTIONNAIRE-TRUE-HH-UPRN", 5);
    allowanceMap.put("POST-QUESTIONNAIRE-TRUE-SPG-UPRN", 5);
    allowanceMap.put("POST-QUESTIONNAIRE-TRUE-CE-UPRN", 50);

    allowanceMap.put("POST-LARGE_PRINT-FALSE-HH-UPRN", 5);
    allowanceMap.put("POST-LARGE_PRINT-FALSE-SPG-UPRN", 5);

    allowanceMap.put("POST-LARGE_PRINT-TRUE-HH-UPRN", 10);
    allowanceMap.put("POST-LARGE_PRINT-TRUE-SPG-UPRN", 10);
    allowanceMap.put("POST-LARGE_PRINT-TRUE-CE-UPRN", 50);

    allowanceMap.put("POST-CONTINUATION-FALSE-HH-UPRN", 12);
    allowanceMap.put("POST-CONTINUATION-FALSE-SPG-UPRN", 12);

    allowanceMap.put("POST-IP", 50);

    allowanceMap.put("IP", 100);

    setupTimeMaps();
  }

  private void setupTimeMaps() {
    allowanceMap.forEach((key, value) -> postingsTimeMap.put(key, getNewTimeMap()));
  }

  private Map<String, List<Integer>> getNewTimeMap() {
    return new HashMap<>();
  }

  public void resetLimiterMaps() {
    postingsTimeMap.clear();
    setupTimeMaps();
  }

  private int getDayHourNow() {
    SimpleDateFormat dayHourDateFormatter = new SimpleDateFormat("DDDHH");
    final Date now = new Date(System.currentTimeMillis());
    return Integer.parseInt(dayHourDateFormatter.format(now));
  }
}
