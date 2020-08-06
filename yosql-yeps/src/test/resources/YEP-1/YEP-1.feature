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

  Scenario: Developer uses standard read method
    When the standard read method is called
    Then data should be returned from the database
