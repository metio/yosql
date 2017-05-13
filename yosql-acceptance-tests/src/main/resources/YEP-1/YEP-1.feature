Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database.

  Background:
    Given database has data

  Scenario: Developer uses standard read method
    When the standard read method is called
    Then data from the database should be returned

  Scenario: Developer uses eager stream read method
    When the stream read method is called
    Then data from the database should be returned

  Scenario: Developer uses lazy stream read method
    When the stream lazy read method is called
    Then data from the database should be returned

  Scenario: Developer uses RxJava2 read method
    When the rxjava read method is called
    Then data from the database should be returned
