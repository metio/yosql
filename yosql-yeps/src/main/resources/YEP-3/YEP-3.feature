Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard SELECT statement.

  Background:
    Given database has data

  Scenario: Developer uses lazy stream read method
    When the stream_lazy read method is called
    Then data from the database should be returned
