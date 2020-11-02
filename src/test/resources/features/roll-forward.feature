Feature: This feature tests that tests can be rerun once the hour ticks over and the limiter is refreshed
  I want to test Fulfilment Journeys using the Rate Limiter after waiting 1 hour

  Scenario Outline: Roll Forward - UPRN TEST
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    And I wait until the hour
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "11111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "11112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "11113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "11114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "11115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "11116" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "11117" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "SPG"       | "false"    | "11118" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "CE"        | "false"    | "11119" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "HH"        | "true"     | "11120" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "SPG"       | "true"     | "11121" |
      | 100           |   50           | 50             | "UAC"         | "POST"          | "CE"        | "true"     | "11122" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "HH"        | "false"    | "11123" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "false"    | "11124" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "HH"        | "true"     | "11125" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "11126" |
      | 100           |   50           | 50             |"QUESTIONNAIRE"| "POST"          | "CE"        | "true"     | "11127" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "HH"        | "false"    | "11128" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "SPG"       | "false"    | "11129" |

  Scenario Outline: Roll Forward - TELEPHONE TEST
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo>
    And I wait until the hour
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo       |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "111114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "111115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "111116" |

  Scenario Outline: Roll Forward - IP ADDRESS TEST
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> ip <ip>
    And I wait until the hour
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | ip       |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "false"    | ".1.1.1"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "false"    | ".1.1.2"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "false"    | ".1.1.3"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "true"     | ".1.1.4"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "true"     | ".1.1.5"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | ".1.1.6"|
