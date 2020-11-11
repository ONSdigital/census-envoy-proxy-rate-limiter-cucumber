Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACPostSPGNonIndividual
  Scenario Outline: Combinations TEST - UAC POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.2.0"  | "911112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.2.0"  | "911113" |
      | 10            | 0              | 10             | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.2.1"  | "911114" |
      | 10            | 0              | 10             | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.2.1"  | "911115" |

  @UACPostSPGIndividual
  Scenario Outline: Combinations TEST - UAC POST SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.1.0"  | "911112" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.1.0"  | "911113" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.1.2"  | "911114" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.1.2"  | "911115" |