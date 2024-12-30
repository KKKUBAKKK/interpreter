package com.github.KKKUBAKKK

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args: Array<String>) {
    // Read program from a text file given as a command-line argument
    val program = if (args.isNotEmpty()) {
        File(args[0]).readText()
    } else {
        println("Please provide a program file as a command-line argument")
        return
    }
    
    // Execute the program
    val interpreter = Interpreter()
    interpreter.execute(program)
}