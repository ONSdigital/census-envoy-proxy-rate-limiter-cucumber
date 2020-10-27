Feature: A Test
  I want to test Fulfilment Journeys using the Rate Limiter

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "1111117" |


  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo       |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "07787111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "07787111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "07787111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "07787111114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "07787111115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "07787111116" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> ip <ip>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | ip       |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "false"    | "1.1.1.1"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "false"    | "1.1.1.2"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "false"    | "1.1.1.3"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "true"     | "1.1.1.4"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "true"     | "1.1.1.5"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "1.1.1.6"|
