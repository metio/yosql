@YEP-1
Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard read statement.

  @YEP-1-0 @YEP-1-0-0
  Scenario: Using YoSQL's default configuration
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
