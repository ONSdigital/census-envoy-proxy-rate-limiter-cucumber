Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACPostHHNonIndividual
  Scenario Outline: Combinations TEST - UAC POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711113" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711115" |

  @UACPostHHIndividual
  Scenario Outline: Combinations TEST - UAC POST HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811112" |
      | 85            | 5              | 80             | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811113" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811114" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811115" |