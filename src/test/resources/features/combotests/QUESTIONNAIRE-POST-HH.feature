Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  @QuestionnairePostHHNonIndividual
  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType   | individual | telNo     | ipAddress  | uprn     |
      | 6             | 1              | 5              |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0100000" | ".0.80.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0200000" | ".0.80.0"  | "911112" |
      | 10            | 1              | 9              |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0300000" | ".0.20.0"  | "911114" |

  @QuestionnairePostHHIndividual
  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType   | individual | telNo     | ipAddress  | uprn     |
      | 6             | 5              | 1              |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0001000" | ".0.80.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0002000" | ".0.80.0"  | "911112" |
      | 10            | 5              | 5              |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0003000" | ".0.20.0"  | "911114" |