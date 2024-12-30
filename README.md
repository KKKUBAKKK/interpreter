# Interpreter for a Simple Programming Language

## Overview

This repository contains an interpreter for a simple programming language that supports variable assignments, scoped operations, and printing of variable values. The interpreter is written in Kotlin and is designed to execute programs that follow specific syntax rules.

## Supported Operations

The language supports the following operations:

1. **Variable Assignment:**
   - Assign an integer value to a variable.
     ```plaintext
     x = 99  # Syntax: <name> = <integer value>
     ```
   - Assign a variable to another variable's value.
     ```plaintext
     x = y  # Syntax: <name> = <another name>
     ```

2. **Scoped Operations:**
   - Open a scope.
     ```plaintext
     scope {
     ```
   - Exit the last opened scope.
     ```plaintext
     }
     ```

3. **Print Variable Value:**
   - Print the value of a variable. If the variable does not exist, print "null".
     ```plaintext
     print x  # Syntax: print <variable name>
     ```

## Language Rules

- It is allowed to re-assign existing variables. For example:
  ```plaintext
  x = 5
  x = 7
  ```
- All declarations and re-assignments of variables inside any scope (including all nested scopes) disappear after exiting the scope.
- If an undeclared variable is used it always returns null (both in assignment and in print)
- There can alwayc be only one operation performed in one line, except for beginning and ending scope. After the beginning of the scope and before ending it there be one extra operation in the line.
- The beginning of scope, one instruction and the scopes end can be in one line.
- The opening braces can be in different line than 'scope', as long as thhey are the first instruction after it.
- Number of spaces or tabs doesn't matter.
- Number od newlines also doesn't matter as long as operations are correctly separated by them.

## Example Program and Output

Consider the following program:

```plaintext
x = 1
print x
scope {
 x = 2
 print x
 scope {
   x = 3
   y = x
   print x
   print y
 }
 print x
 print y
}
print x
```

The expected output of the program is:

```plaintext
1
2
3
3
2
null
1
```

## How to Use the Interpreter

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/KKKUBAKKK/interpreter.git
   cd interpreter
   ```

2. **Build the Project:**
   Use your preferred Kotlin build tool (e.g., Gradle or Maven) to build the project.

3. **Run the Interpreter:**
   Execute the interpreter with path to a program file as command line input. Ensure the program file follows the syntax rules mentioned above.
