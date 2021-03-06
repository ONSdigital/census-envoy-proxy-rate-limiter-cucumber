Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @UACPostCENonIndividual
  Scenario Outline: Combinations TEST - UAC POST CE Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.4"  | "911112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.4"  | "911113" |
      | 10            | 0              | 10             | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.4"  | "911114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.5"  | "911115" |

  @UACPostCEIndividual
  Scenario Outline: Combinations TEST - UAC POST CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType | individual | telNo     | ipAddress | uprn     |
      | 30            | 30             | 0              | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.1"  | "911112" |
      | 85            | 20             | 65             | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.1"  | "911112" |
      | 10            | 0              | 10             | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.1"  | "911114" |
      | 10            | 10             | 0              | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.2"  | "911115" |