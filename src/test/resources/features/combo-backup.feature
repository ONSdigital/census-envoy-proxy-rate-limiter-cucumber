Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  Scenario Outline: Combinations TEST
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> access code <accessCode> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy poxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | accessCode  | individual | telNo    | ipAddress | uprn    |
      | 20            |   5            | 15             | "UAC"         | "SMS"           | "HH"        | "false"    | "1222222" | ".0.0.0"  | "111111" |
      | 20            |   5            | 15             | "UAC"         | "SMS"           | "SPG"       | "false"    | "1222222" | ".0.0.0"  | "111111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "1222222" | ".0.0.0"  | "111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "1222222" | ".0.0.0"  | "111111" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "1222222" | ".0.0.0"  | "111111" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "1222222" | ".0.0.0"  | "111111" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   1            | 9              | "UAC"         | "POST"          | "CE"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "HH"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              | "UAC"         | "POST"          | "SPG"       | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 100           |   50           | 50             | "UAC"         | "POST"          | "CE"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "HH"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 100           |   50           | 50             |"QUESTIONNAIRE"| "POST"          | "CE"        | "true"     | "1333333" | ".0.0.1"  | "111112" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            |   6            | 4              | "UAC"         | "SMS"           | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            |   0            | 10             | "UAC"         | "SMS"           | "CE"        | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 20            |   0            | 20             | "UAC"         | "SMS"           | "HH"        | "true"     | "1333333" | ".0.0.1"  | "111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "SPG"       | "true"     | "1444444" | ".0.0.2"  | "111113" |
      | 100           |   50           | 50             | "UAC"         | "SMS"           | "CE"        | "true"     | "1444444" | ".0.0.2"  | "111113" |
      | 10            |   0            | 10             | "UAC"         | "SMS"           | "HH"        | "false"    | "1444444" | ".0.0.2"  | "111113" |
      | 20            |   10           | 10             | "UAC"         | "SMS"           | "HH"        | "true"     | "1444444" | ".0.0.2"  | "111113" |
      | 20            |   0            | 20             | "UAC"         | "SMS"           | "SPG"       | "true"     | "1444444" | ".0.0.2"  | "111113" |
      | 100           |   0            | 100            | "UAC"         | "SMS"           | "CE"        | "true"     | "1444444" | ".0.0.2"  | "111113" |
      | 10            |   0            | 10             | "UAC"         | "SMS"           | "SPG"       | "false"    | "1444444" | ".0.0.2"  | "111113" |
      | 10            |   5            | 5              | "UAC"         | "SMS"           | "CE"        | "false"    | "1444444" | ".0.0.2"  | "111113" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            |   1            | 9              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "HH"        | "true"     | "1333333" | ".0.0.1"  | "111113" |
      | 10            |   5            | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "1333333" | ".0.0.1"  | "111113" |
      | 100           |   50           | 50             |"QUESTIONNAIRE"| "POST"          | "CE"        | "true"     | "1333333" | ".0.0.1"  | "111113" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "HH"        | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 20            |   12           | 8              |"CONTINUATION" | "POST"          | "SPG"       | "false"    | "1333333" | ".0.0.1"  | "111113" |