Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType    | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 1              | 9              |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType    | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 5              | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 40            | 40             | 0              |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 20            | 10             | 10             |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 60            | 50             | 10             |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911114" |