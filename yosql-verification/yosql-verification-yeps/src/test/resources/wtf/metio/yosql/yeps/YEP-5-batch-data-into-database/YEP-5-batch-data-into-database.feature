@YEP-5
Feature: Batch data into a database

  Developers should be able to use the generated code
  to batch data into a database.

  @YEP-5-0 @YEP-5-0-0
  Scenario: Using YoSQL's default configuration
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
