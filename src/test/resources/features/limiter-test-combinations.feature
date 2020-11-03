Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

Scenario Outline: Combinations TEST
Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
When I post the fulfilments to the envoy poxy client
Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

  Examples:
| noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo    | ipAddress | uprn    |
| 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111112" |
| 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111113" |
