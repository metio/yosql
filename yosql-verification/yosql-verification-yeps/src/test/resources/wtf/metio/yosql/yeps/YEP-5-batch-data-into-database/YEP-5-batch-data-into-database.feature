@YEP-5
Feature: Write data into a database

  Developers should be able to use the generated code
  to batch data into a database.

  @YEP-5-0 @YEP-5-0-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given the JDBC API is used
    And YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
