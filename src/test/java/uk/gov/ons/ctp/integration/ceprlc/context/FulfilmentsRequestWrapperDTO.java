package uk.gov.ons.ctp.integration.ceprlc.context;

import lombok.Data;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentsRequestDTO;

@Data
public class FulfilmentsRequestWrapperDTO {

  private FulfilmentsRequestDTO fulfilmentsRequestDTO;
  private String uprn;
  private String ipAddress;
  private boolean indvidual;
  private String telNo;
}
