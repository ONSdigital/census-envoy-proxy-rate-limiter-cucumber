Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACSMSHHNonIndividual
  Scenario Outline: Combinations TEST - UAC SMS HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "SMS"           | "HH"      | "false"    | "3333333" | ".0.0.3"  | "311112" |
      | 85            | 4              | 81             | "UAC"        | "SMS"           | "HH"      | "false"    | "3333333" | ".0.0.3"  | "311113" |
      | 10            | 5              | 5              | "UAC"        | "SMS"           | "HH"      | "false"    | "3333334" | ".0.0.3"  | "311114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "HH"      | "false"    | "3333335" | ".0.0.3"  | "311115" |

  @UACSMSHHIndividual
  Scenario Outline: Combinations TEST - UAC SMS HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 11            | 10             | 1              | "UAC"        | "SMS"           | "HH"      | "true"     | "4333333" | ".0.0.4"  | "411112" |
      | 85            | 0              | 85             | "UAC"        | "SMS"           | "HH"      | "true"     | "4333333" | ".0.0.4"  | "411113" |
      | 10            | 4              | 6              | "UAC"        | "SMS"           | "HH"      | "true"     | "4333334" | ".0.0.4"  | "411114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "HH"      | "true"     | "4333335" | ".0.0.4"  | "411115" |