Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACPostHHNonIndividual
  Scenario Outline: Combinations TEST - UAC POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "HH"      | "false"    | "0000001" | ".0.3.0"  | "711112" |
      | 1             | 1              | 0              | "UAC"        | "POST"           | "HH"      | "false"    | "0000002" | ".0.3.0"  | "711113" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000003" | ".0.2.1"  | "711114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000004" | ".0.2.1"  | "711115" |

  @UACPostHHIndividual
  Scenario Outline: Combinations TEST - UAC POST HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "POST"           | "HH"      | "true"     | "0000005" | ".0.8.7"  | "811112" |
      | 10            | 0              | 10             | "UAC"        | "POST"           | "HH"      | "true"     | "0000006" | ".0.8.7"  | "811112" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000007" | ".0.2.1"  | "801114" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000008" | ".0.2.1"  | "801115" |