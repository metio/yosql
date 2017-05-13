Feature: Write data into a database

  Developers should be able to use the generated code 
  to write data into a database.

  Background:
    Given database has schema

  Scenario: Developer uses standard write method
    When the standard write method is called
    Then data is written into the database

  Scenario: Developer uses batch write method
    When the batch write method is called
    Then data is written into the database
