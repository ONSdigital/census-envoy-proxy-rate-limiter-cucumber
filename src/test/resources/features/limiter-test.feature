Feature: A Test
  I want to test Fulfilment Journeys using the Rate Limiter

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "2111117" |


  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo       |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "207787111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "207787111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "207787111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "207787111114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "207787111115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "207787111116" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> ip <ip>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | ip       |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "false"    | "21.1.1.1"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "false"    | "21.1.1.2"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "false"    | "21.1.1.3"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "true"     | "21.1.1.4"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "true"     | "21.1.1.5"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "21.1.1.6"|
