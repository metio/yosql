Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard SELECT statement.

  Background:
    Given database has data

  Scenario: Developer uses RxJava2 read method
    When the rxjava read method is called
    Then data from the database should be returned
