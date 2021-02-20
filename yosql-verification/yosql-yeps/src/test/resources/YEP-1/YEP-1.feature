@YEP-1
Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database using a standard read statement.

  Each dao generator decides on its own what 'standard' means.
  The JDBC implementation for example uses the 'executeQuery'
  method of a 'PreparedStatement'.

  The generated method has to yield the data which is
  returned by the database.

  Background:
    Given database has schema
    And database has data

  @YEP-1-1
  Scenario: Developer uses standard read method
    When the standard read method is called
    Then data should be returned from the database

  @YEP-1-2
  Scenario: Developer uses eager stream read method
    When the stream read method is called
    Then data from the database should be returned

  @YEP-1-3
  Scenario: Developer uses lazy stream read method
    When the stream_lazy read method is called
    Then data from the database should be returned

  @YEP-1-4
  Scenario: Developer uses RxJava2 read method
    When the rxjava read method is called
    Then data from the database should be returned
