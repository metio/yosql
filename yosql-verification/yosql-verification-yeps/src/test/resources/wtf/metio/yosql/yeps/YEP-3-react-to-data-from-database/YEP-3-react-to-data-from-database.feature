@YEP-3
Feature: React to data from a database

  Developers should be able to use the generated code to react
  to data from a database by using a reactive stream of data.

  Each dao generator decides on its own how that's implemented.

  @YEP-3-0 @YEP-3-0-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given YoSQL uses its defaults configuration
    And JDBC persistence is used
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
