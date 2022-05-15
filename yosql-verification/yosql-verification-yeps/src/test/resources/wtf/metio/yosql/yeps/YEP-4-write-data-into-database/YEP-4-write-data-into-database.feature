@YEP-4
Feature: Write data into a database

  Developers should be able to use the generated code
  to write data into a database.

  @YEP-4 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a write method
    Then the generated methods looks as expected
