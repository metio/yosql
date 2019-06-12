Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard SELECT statement.

  Background:
    Given database has data

  Scenario: Developer uses eager stream read method
    When the stream read method is called
    Then data from the database should be returned
