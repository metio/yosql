Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database.

  Rules:
  - Database connection must be available

  Scenario: Developer uses standard read method
    Given A repository exists
    When the standard read method is called
    Then data from a database should be returned

  Scenario: Developer uses eager stream read method
    Given A repository exists
    When the stream read method is called
    Then data from a database should be returned

  Scenario: Developer uses lazy stream read method
    Given A repository exists
    When the stream lazy read method is called
    Then data from a database should be returned

  Scenario: Developer uses RxJava2 read method
    Given A repository exists
    When the rxjava read method is called
    Then data from a database should be returned
