Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACSMSSPGNonIndividual
  Scenario Outline: Combinations TEST - UAC SMS SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 85            | 4              | 81             | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            | 5              | 5              | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333334" | ".0.0.1"  | "111114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333335" | ".0.0.1"  | "111115" |

  @UACSMSSPGIndividual
  Scenario Outline: Combinations TEST - UAC SMS SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 11            | 11             | 0              | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333333" | ".0.0.2"  | "211112" |
      | 85            | 85             | 0              | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333333" | ".0.0.2"  | "211113" |
      | 10            | 4              | 6              | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333334" | ".0.0.2"  | "211114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333335" | ".0.0.2"  | "211115" |