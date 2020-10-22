package uk.gov.ons.ctp.integration.ceprlc.mockclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.error.CTPException.Fault;
import uk.gov.ons.ctp.integration.ceprlc.context.FulfilmentsRequestWrapperDTO;

@Data
@NoArgsConstructor
@Component
public class MockClient {

  private Map<String, Integer> allowanceMap = new HashMap<>();
  private Map<String, Map<String, List<Long>>> postingsTimeMap = new HashMap<>();

  public int postFulfilment(
      final FulfilmentsRequestWrapperDTO fulfilmentsRequestWrapperDTORequestDTO)
      throws CTPException {

    List<String> requestKeyList = getKeys(fulfilmentsRequestWrapperDTORequestDTO);
    if (!isValidateRequest(requestKeyList, fulfilmentsRequestWrapperDTORequestDTO)) {
      throw new CTPException(Fault.BAD_REQUEST);
    }
    postRequest(requestKeyList, fulfilmentsRequestWrapperDTORequestDTO);
    return HttpStatus.SC_OK;
  }

  private List<String> getKeys(FulfilmentsRequestWrapperDTO f) {
    List<String> keyList = new ArrayList<>();
    StringBuilder keyBuff = new StringBuilder(
        f.getFulfilmentsRequestDTO().getProductGroup().name().toUpperCase())
        .append("-")
        .append(Boolean.valueOf(f.isIndvidual()).toString().toUpperCase())
        .append("-")
        .append(f.getFulfilmentsRequestDTO().getDeliveryChannel().name().toUpperCase())
        .append("-")
        .append(f.getFulfilmentsRequestDTO().getCaseType().name().toUpperCase())
        .append("-");
    if (f.getUprn() != null) {
      keyList.add(keyBuff.toString() + "UPRN");
    }
    if (f.getIpAddress() != null) {
      keyList.add(keyBuff.toString() + "IP");
    }
    if (f.getTelNo() != null) {
      keyList.add(keyBuff.toString() + "TELNO");
    }
    return keyList;
  }

  private boolean isValidateRequest(List<String> requestKeyList, FulfilmentsRequestWrapperDTO f) {
    boolean isValidRequest = true;
    for (String requestKey : requestKeyList) {
      final int noRequestAllowed = allowanceMap.get(requestKey);
      Map<String, List<Long>> postedMap = postingsTimeMap.get(requestKey);
      if (postedMap == null) {
        return isValidRequest;
      }
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];
      List<Long> postingsList = null;
      if (keyType.equals("UPRN")) {
        postingsList = postedMap.get(f.getUprn());
      }
      if (keyType.equals("IP")) {
        postingsList = postedMap.get(f.getIpAddress());
      }
      if (keyType.equals("TELNO")) {
        postingsList = postedMap.get(f.getTelNo());
      }
      if (postingsList == null) {
        continue;
      }
      final Date now = new Date(System.currentTimeMillis());
      final Date oneHourAgo = DateUtils.addHours(now, -1);
      final Long oneHourAgoLong = oneHourAgo.getTime();
      int postsWithinScopeCount = 0;
      for (Long postDateTime : postingsList) {
        if (postDateTime > oneHourAgoLong) {
          postsWithinScopeCount++;
        }
      }
      if (postsWithinScopeCount >= noRequestAllowed) {
        isValidRequest = false;
      }
    }
    return isValidRequest;
  }

  private void postRequest(List<String> requestKeyList, FulfilmentsRequestWrapperDTO f) {

    for (String requestKey : requestKeyList) {
      Map<String, List<Long>> postedMap = postingsTimeMap.get(requestKey);
      if (postedMap == null) {
        return;
      }
      final String[] keyConstituents = requestKey.split("-");
      final String keyType = keyConstituents[keyConstituents.length - 1];
      List<Long> postingsList = null;
      String postingsListKey = null;
      if (keyType.equals("UPRN")) {
        postingsListKey = f.getUprn();
      }
      if (keyType.equals("IP")) {
        postingsListKey = f.getIpAddress();
      }
      if (keyType.equals("TELNO")) {
        postingsListKey = f.getTelNo();
      }
      postedMap.computeIfAbsent(postingsListKey, k -> new ArrayList<>());
      final Date now = new Date(System.currentTimeMillis());
      final Long nowLong = now.getTime();
      postedMap.get(postingsListKey).add(nowLong);
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
    allowanceMap.put("CONTINUATION-FALSE-POST-HH-UPRN", 1);
    allowanceMap.put("CONTINUATION-FALSE-POST-SPG-UPRN", 1);

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

  private Map<String, List<Long>> getNewTimeMap() {
    return new HashMap<>();
  }

  public void waitHours(final int hours) {
    postingsTimeMap.forEach( (key1, value1) -> {
      value1.forEach( (key2, value2) -> {
        final List<Long> updatedTimeList = new ArrayList<>();
        value2.forEach( timeValue -> {
          final Date transactionDate = new Date(timeValue);
          final Date minusHours = DateUtils.addHours(transactionDate, (hours* -1));
          updatedTimeList.add(minusHours.getTime());
        });
        value1.put(key2,updatedTimeList);
      });
    });
  }
}
