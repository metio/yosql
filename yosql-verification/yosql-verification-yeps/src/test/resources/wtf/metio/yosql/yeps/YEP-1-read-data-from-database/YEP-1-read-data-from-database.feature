@YEP-1
Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard read statement.

  Each dao generator decides on its own how that's implemented.
  The JDBC implementation for example uses the 'executeQuery'
  method of a 'PreparedStatement'.

  @YEP-1-0-0 @JDBC
  Scenario: Using YoSQL's default configuration and the JDBC API
    Given the JDBC API is used
    And YoSQL uses its defaults configuration
    When an SQL statement gets converted to a read method
    Then the generated methods looks as expected
