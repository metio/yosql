@YEP-2
Feature: Stream data from a database

  Developers should be able to use the generated code to read
  data from a database by using a stream of data.

  Each dao generator decides on its own how that's implemented.

  @YEP-2-0 @YEP-2-0-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected

  @YEP-2-1 @YEP-2-1-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
