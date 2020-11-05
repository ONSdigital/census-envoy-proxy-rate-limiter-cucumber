Feature: This feature tests all of the requirements for the Envoy Proxy Limiter - as combinations of requests telephone, ipAddress and uprn
  I want to test Fulfilment Journeys using the Rate Limiter for all combinations

  Scenario Outline: Combinations TEST - UAC SMS SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333333" | ".0.0.1"  | "111112" |
      | 85            | 4              | 81             | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333333" | ".0.0.1"  | "111113" |
      | 10            | 5              | 5              | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333334" | ".0.0.1"  | "111114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "SPG"      | "false"    | "1333335" | ".0.0.1"  | "111115" |

  Scenario Outline: Combinations TEST - UAC SMS SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 11            | 10             | 1              | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333333" | ".0.0.2"  | "211112" |
      | 85            | 0              | 85             | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333333" | ".0.0.2"  | "211113" |
      | 10            | 4              | 6              | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333334" | ".0.0.2"  | "211114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "SPG"      | "true"     | "2333335" | ".0.0.2"  | "211115" |

  Scenario Outline: Combinations TEST - UAC SMS HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "SMS"           | "HH"      | "false"    | "3333333" | ".0.0.3"  | "311112" |
      | 85            | 4              | 81             | "UAC"        | "SMS"           | "HH"      | "false"    | "3333333" | ".0.0.3"  | "311113" |
      | 10            | 5              | 5              | "UAC"        | "SMS"           | "HH"      | "false"    | "3333334" | ".0.0.3"  | "311114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "HH"      | "false"    | "3333335" | ".0.0.3"  | "311115" |

  Scenario Outline: Combinations TEST - UAC SMS HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel | caseType  | individual | telNo     | ipAddress | uprn     |
      | 11            | 10             | 1              | "UAC"        | "SMS"           | "HH"      | "true"     | "4333333" | ".0.0.4"  | "411112" |
      | 85            | 0              | 85             | "UAC"        | "SMS"           | "HH"      | "true"     | "4333333" | ".0.0.4"  | "411113" |
      | 10            | 4              | 6              | "UAC"        | "SMS"           | "HH"      | "true"     | "4333334" | ".0.0.4"  | "411114" |
      | 10            | 0              | 10             | "UAC"        | "SMS"           | "HH"      | "true"     | "4333335" | ".0.0.4"  | "411115" |

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

  Scenario Outline: Combinations TEST - UAC POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711113" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "HH"      | "false"    | "0000000" | ".0.0.0"  | "711115" |

  Scenario Outline: Combinations TEST - UAC POST HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811112" |
      | 85            | 5              | 80             | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811113" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811114" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "HH"      | "true"     | "0000000" | ".0.0.0"  | "811115" |

  Scenario Outline: Combinations TEST - UAC POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.0.0"  | "911113" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.0.0"  | "911114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "SPG"     | "false"    | "0000000" | ".0.0.0"  | "911115" |

  Scenario Outline: Combinations TEST - UAC POST SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType  | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 85            | 5              | 80             | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.0.0"  | "911113" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.0.0"  | "911114" |
      | 10            | 5              | 5              | "UAC"        | "POST"           | "SPG"     | "true"     | "0000000" | ".0.0.0"  | "911115" |

  Scenario Outline: Combinations TEST - UAC POST CE Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 85            | 1              | 84             | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.0"  | "911113" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.0"  | "911114" |
      | 10            | 1              | 9              | "UAC"        | "POST"           | "CE"     | "false"    | "0000000" | ".0.0.0"  | "911115" |

  Scenario Outline: Combinations TEST - UAC POST CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup | deliveryChannel  | caseType | individual | telNo     | ipAddress | uprn     |
      | 30            | 30             | 0              | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 85            | 20             | 65             | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 10             | 0              | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.0"  | "911114" |
      | 10            | 10             | 0              | "UAC"        | "POST"           | "CE"     | "true"     | "0000000" | ".0.0.0"  | "911115" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType   | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 1              | 9              |"QUESTIONNAIRE"| "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST HH Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 5              | 5              |"QUESTIONNAIRE"| "POST"          | "HH"       | "true"     | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType    | individual | telNo     | ipAddress | uprn     |
      | 6             | 1              | 5              |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 1              | 9              |"QUESTIONNAIRE"| "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST SPG Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType    | individual | telNo     | ipAddress | uprn     |
      | 6             | 5              | 1              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 0              | 10             |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 10            | 5              | 5              |"QUESTIONNAIRE"| "POST"          | "SPG"       | "true"     | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - QUESTIONNAIRE POST CE Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel | caseType   | individual | telNo     | ipAddress | uprn     |
      | 40            | 40             | 0              |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 20            | 10             | 10             |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911112" |
      | 60            | 50             | 10             |"QUESTIONNAIRE"| "POST"          | "CE"       | "true"     | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - CONTINUATION POST HH Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType   | individual | telNo     | ipAddress | uprn     |
      | 7             | 7              | 0              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 7             | 5              | 2              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 13            | 12             | 1              |"CONTINUATION" | "POST"           | "HH"       | "false"    | "0000000" | ".0.0.0"  | "911114" |

  Scenario Outline: Combinations TEST - CONTINUATION POST SPG Non-Individual
    Given I have <noFulfilments> fulfilment requests of product group <productGroup> delivery channel <deliveryChannel> case type <caseType> individual is <individual> telephone <telNo> ipAddress <ipAddress> uprn <uprn>
    When I post the fulfilments to the envoy proxy client
    Then I expect the first <expectedToPass> calls to succeed and <expectedToFail> calls to fail

    Examples:
      | noFulfilments | expectedToPass | expectedToFail | productGroup  | deliveryChannel  | caseType    | individual | telNo     | ipAddress | uprn     |
      | 7             | 7              | 0              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 7             | 5              | 2              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911112" |
      | 13            | 12             | 1              |"CONTINUATION" | "POST"           | "SPG"       | "false"    | "0000000" | ".0.0.0"  | "911114" |
