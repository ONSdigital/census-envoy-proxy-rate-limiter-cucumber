Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @LargePrintPostSPGNonIndividual
  Scenario Outline: Combinations TEST - LARGE_PRINT POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType    | individual | telNo     | ipAddress | uprn     |
      | 15            | 10             | 5              |"LARGE_PRINT"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.7.9"  | "921112" |
      | 10            | 0              | 10             |"LARGE_PRINT"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.8.9"  | "921112" |
      | 10            | 10             | 0              |"LARGE_PRINT"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.9.9"  | "921114" |

  @LargePrintPostSPGIndividual
  Scenario Outline: Combinations TEST - LARGE_PRINT POST SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType    | individual | telNo     | ipAddress | uprn     |
      | 15            | 10             | 5              |"LARGE_PRINT"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.7"  | "921112" |
      | 10            | 0              | 10             |"LARGE_PRINT"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.1.7"  | "921112" |
      | 10            | 10             | 0              |"LARGE_PRINT"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.2.7"  | "921114" |

  @LargePrintPostCEIndividual
  Scenario Outline: Combinations TEST - LARGE_PRINT POST CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 40            | 40             | 0              |"LARGE_PRINT"| "POST"          | "CE"       | "true"     | "0000000" | ".0.9.0"  | "921112" |
      | 20            | 10             | 10             |"LARGE_PRINT"| "POST"          | "CE"       | "true"     | "0000000" | ".0.2.1"  | "921112" |
      | 60            | 50             | 10             |"LARGE_PRINT"| "POST"          | "CE"       | "true"     | "0000000" | ".0.2.2"  | "921114" |