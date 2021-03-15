@YEP-6
Feature: Call database procedure

  Developers should be able to use the generated code
  to call a database procedure.

  @YEP-6-0 @YEP-6-0-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given YoSQL uses its defaults configuration
    And JDBC persistence is used
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
