Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @ContinuationPostHHNonIndividual
  Scenario Outline: Combinations TEST - CONTINUATION POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType   | individual | telNo     | ipAddress  | uprn     |
      | 7             | 7              | 0              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".10.0.0"  | "911112" |
      | 7             | 5              | 2              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".10.0.0"  | "911112" |
      | 13            | 12             | 1              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".10.0.0"  | "911114" |
      | 13            | 13             | 0              |"CONTINUATION" | "POST"           | "HH"       | "true"     | "0000000" | ".10.0.0"  | "911114" |

  @ContinuationPostSPGNonIndividual
  Scenario Outline: Combinations TEST - CONTINUATION POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType    | individual | telNo     | ipAddress  | uprn     |
      | 7             | 7              | 0              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".10.0.0"  | "911112" |
      | 7             | 5              | 2              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".10.0.0"  | "911112" |
      | 13            | 12             | 1              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".10.0.0"  | "911114" |