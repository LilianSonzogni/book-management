Feature: Manage books

  Scenario: the user adds books and retrieves them sorted by title
    Given the user adds the book "The Pragmatic Programmer" by "David Thomas"
    And the user adds the book "Clean Code" by "Robert C. Martin"
    When the user retrieves all the books
    Then the returned list should contain the following books
      | title                   | author            |
      | Clean Code               | Robert C. Martin  |
      | The Pragmatic Programmer | David Thomas      |
