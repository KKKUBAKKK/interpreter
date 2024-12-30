package com.github.KKKUBAKKK

import java.util.*

class Interpreter {
    private val scopes = Stack<MutableMap<String, Int?>>()
    
    init {
        scopes.push(mutableMapOf())
    }

    fun execute(program: String) {
        // Converting all whitespace sequences to single corresponding whitespace character
        val normalizedProgram = program
            .replace(Regex("\\n+"), "\n")                       // Convert all newlines to a single newline
            .replace(Regex(" +"), " ")                          // Convert all spaces to a single space
            .replace(Regex("\\t+"), "\t")                       // Convert all tabs to a single tab
            .replace(Regex("^\\s+", RegexOption.MULTILINE), "") // Remove leading whitespaces
            .trim()

        val currentToken = StringBuilder()
        val tokens = mutableListOf<String>()
        var openingBraces = 0
        var closingBraces = 0

        // Tokenize the program, handling braces as separate tokens
        for (char in normalizedProgram) {
            when (char) {
                '{' -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString().trim())
                        currentToken.clear()
                    }
                    openingBraces++
                    tokens.add(char.toString())
                }
                '}' -> {
                    if (openingBraces <= closingBraces) {
                        throw IllegalArgumentException("Invalid syntax: '}' without matching '{'")
                    }
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString().trim())
                        currentToken.clear()
                    }
                    closingBraces++
                    tokens.add(char.toString())
                }
                ' ' -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString().trim())
                        currentToken.clear()
                    }
                }
                '\n' -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString().trim())
                        currentToken.clear()
                    }
                    tokens.add(char.toString())
                }
                else -> currentToken.append(char)
            }
        }
        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken.toString().trim())
        }
        if (openingBraces != closingBraces) {
            throw IllegalArgumentException("Invalid syntax: $openingBraces opening braces, $closingBraces closing braces")
        }

        // Process tokens
        var i = 0
        while (i < tokens.size) {
            when {
                tokens[i] == "scope" && i + 1 < tokens.size && tokens[i + 1] == "{" -> {
                    openScope()
                    i += 2 // Skip both "scope" and "{"
                }
                tokens[i] == "scope" && tokens[i + 1] == "\n" && i + 2 < tokens.size && tokens[i + 2] == "{" -> {
                    openScope()
                    i += 3 // Skip "scope", "\n", and "{"
                }
                tokens[i] == "{" -> {
                    throw IllegalArgumentException("Invalid syntax: '{' must be preceded by 'scope'")
                }
                tokens[i] == "}" -> {
                    closeScope()
                    i++
                }
                tokens[i] == "print" && i + 1 < tokens.size -> {
                    if (i + 2 < tokens.size && tokens[i + 2] != "\n" && tokens[i + 2] != "}") {
                        throw IllegalArgumentException("Invalid syntax: only one statement allowed per line")
                    }
                        
                    handlePrint(tokens[i + 1])
                    i += 2
                }
                tokens[i] == "\n" -> {
                    i++
                }
                else -> {
                    // Check if it's an assignment spanning multiple tokens
                    if (i + 2 < tokens.size && tokens[i + 1] == "=") {
                        if (i + 3 < tokens.size && tokens[i + 3] != "\n" && tokens[i + 3] != "}") {
                            throw IllegalArgumentException("Invalid syntax: only one statement allowed per line")
                        }
                        
                        handleAssignment("${tokens[i]}=${tokens[i + 2]}")
                        i += 3
                    } else {
                        throw IllegalArgumentException("Invalid syntax at token: ${tokens[i]}")
                    }
                }
            }
        }
    }

    // Create a new scope with all the variables from the current scope
    private fun openScope() {
        val newScope = mutableMapOf<String, Int?>()
        if (scopes.isNotEmpty()) {
            newScope.putAll(scopes.peek())
        }
        scopes.push(newScope)
    }

    // Go back to the outer scope
    private fun closeScope() {
        if (scopes.size > 1) {
            scopes.pop()
        } else {
            throw IllegalStateException("Cannot close global scope")
        }
    }

    // Print the value of a variable or null if it doesn't exist
    private fun handlePrint(varName: String) {
        val value = getCurrentScope()[varName]
        println(value ?: "null")
    }

    // Assign a value to a variable
    private fun handleAssignment(line: String) {
        val (name, valueStr) = line.split("=").map { it.trim() }
        val value = valueStr.toIntOrNull() ?: getCurrentScope()[valueStr]
        if (value != null) {
            getCurrentScope()[name] = value
        } else {
            getCurrentScope()[name] = null
        }
    }

    // Get the current scope
    private fun getCurrentScope(): MutableMap<String, Int?> {
        return scopes.peek()
    }
}