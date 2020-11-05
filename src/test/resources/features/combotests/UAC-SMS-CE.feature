Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  Scenario Outline: Combinations TEST - UAC SMS CE Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "SMS"           | "CE"      | "false"    | "5333333" | ".0.0.5"  | "511112" |
      | 85            | 0              | 85             | "UAC"        | "SMS"           | "CE"      | "false"    | "5333333" | ".0.0.5"  | "511113" |
      | 10            | 5              | 5              | "UAC"        | "SMS"           | "CE"      | "false"    | "5333334" | ".0.0.5"  | "511114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "CE"      | "false"    | "5333335" | ".0.0.5"  | "511115" |

  Scenario Outline: Combinations TEST - UAC SMS CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 11            | 11             | 0              | "UAC"        | "SMS"           | "CE"      | "true"     | "6333333" | ".0.0.6"  | "611112" |
      | 85            | 39             | 46             | "UAC"        | "SMS"           | "CE"      | "true"     | "6333333" | ".0.0.6"  | "611113" |
      | 10            | 4              | 6              | "UAC"        | "SMS"           | "CE"      | "true"     | "6333334" | ".0.0.6"  | "611114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "CE"      | "true"     | "6333335" | ".0.0.6"  | "611115" |