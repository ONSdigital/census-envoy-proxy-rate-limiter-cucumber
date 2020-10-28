Feature: A Test
  I want to test Fulfilment Journeys using the Rate Limiter

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "41111111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "41111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "41111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "41111114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "41111115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "41111116" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "41111117" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "SPG"       | "false"    | "41111118" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "CE"        | "false"    | "41111119" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "HH"        | "true"     | "41111120" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "SPG"       | "true"     | "41111121" |
      | 100           |   50           | 50             | "UAC"         | "POST"          | "CE"        | "true"     | "41111122" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "HH"        | "false"    | "41111123" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "false"    | "41111124" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "HH"        | "true"     | "41111125" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "41111126" |
      | 100           |   50           | 50             |"QUESTIONNAIRE"| "POST"          | "CE"        | "true"     | "41111127" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "HH"        | "false"    | "41111128" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "SPG"       | "false"    | "41111129" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo       |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "407787111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "407787111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "407787111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "407787111114" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "407787111115" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "407787111116" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> ip <ip>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | ip       |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "false"    | "41.1.1.1"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "false"    | "41.1.1.2"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "false"    | "41.1.1.3"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "true"     | "41.1.1.4"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "true"     | "41.1.1.5"|
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "41.1.1.6"|