Feature: A Test
  I want to test Fulfilment Journeys using the Rate Limiter after waiting 1 hour

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    And I wait an hour
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "123456" |