Feature: Read data from a database

  Developers should be able to use the generated code to read
  data from a database.

  Rules:
  - Database connection must be available

  Scenario: Developer use standard read method
    Given A repository with a standard read method exists
    When the method is called
    Then data from a database should be returned
