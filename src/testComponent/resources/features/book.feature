Feature: Manage books

  Scenario: the user adds books and retrieves them sorted by title
    Given the user adds the book "The Pragmatic Programmer" by "David Thomas"
    And the user adds the book "Clean Code" by "Robert C. Martin"
    When the user retrieves all the books
    Then the returned list should contain the following books
      | title                    | author           | reserved |
      | Clean Code               | Robert C. Martin | false    |
      | The Pragmatic Programmer | David Thomas     | false    |

  Scenario: the user reserves an available book
    Given the user adds the book "Clean Code" by "Robert C. Martin"
    When the user reserves the book "Clean Code"
    And the user retrieves all the books
    Then the returned list should contain the following books
      | title      | author           | reserved |
      | Clean Code | Robert C. Martin | true     |

  Scenario: a book that is already reserved cannot be reserved again
    Given the user adds the book "Clean Code" by "Robert C. Martin"
    And the user reserves the book "Clean Code"
    When the user tries to reserve the book "Clean Code" again
    Then the reservation is rejected because the book is already reserved
