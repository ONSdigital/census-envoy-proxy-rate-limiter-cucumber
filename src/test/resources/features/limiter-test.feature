Feature: A Test
  I want to test Fulfilment Journeys using the Rate Limiter

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> uprn <uprn>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | uprn     |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "111111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "111111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "111111" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "111111" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "111111" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "SPG"       | "false"    | "111111" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "CE"        | "false"    | "111111" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "HH"        | "true"     | "111111" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "SPG"       | "true"     | "111111" |
      | 100           |   50           | 50             | "UAC"         | "POST"          | "CE"        | "true"     | "111111" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "HH"        | "false"    | "111111" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "false"    | "111111" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "HH"        | "true"     | "111111" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "111111" |
      | 100           |   50           | 50             |"QUESTIONNAIRE"| "POST"          | "CE"        | "true"     | "111111" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "HH"        | "false"    | "111111" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "SPG"       | "false"    | "111111" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo       |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "018118055" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "018118055" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "018118055" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"    | "018118055" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"    | "018118055" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"    | "018118055" |

  Scenario Outline:
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> ip <ip>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail
    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | ip       |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "false"    | "myIP"   |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "false"    | "myIP"   |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "false"    | "myIP"   |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "HH"        | "true"     | "myIP"   |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "SPG"       | "true"     | "myIP"   |
      | 150           |   100          | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "myIP"   |
