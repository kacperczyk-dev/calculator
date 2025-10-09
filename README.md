# calculator
A simple calculator app that supports the following operations and ensures operation precedence (including brackets): 
- addition, 
- subtraction, 
- multiplication, 
- division.

## Approach
1. Given infix equation is first transformed into postfix notation using **shunting-yard** algorithm.
2. Second step is to process the postfix equation using **postfix stack evaluator**.
3. Since **Calculator** class does not have any inner state, I made all its methods static.

## Testing
Simple tests written using JUnit were added to ensure correctness of the implementation.
Run `mvn clean test` to execute all test cases.