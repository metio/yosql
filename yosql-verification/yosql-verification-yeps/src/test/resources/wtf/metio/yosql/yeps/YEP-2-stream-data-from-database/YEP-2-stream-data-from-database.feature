@YEP-2
Feature: Stream data from a database

  Developers should be able to use the generated code to read
  data from a database by using a stream of data.

  @YEP-2-0 @YEP-2-0-0
  Scenario: Using YoSQL's default configuration
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected

  @YEP-2-1 @YEP-2-1-0
  Scenario: Using YoSQL's default configuration
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
