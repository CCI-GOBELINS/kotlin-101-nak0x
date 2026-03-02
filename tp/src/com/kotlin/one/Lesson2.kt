package com.android.one

import kotlin.math.PI

// ─── LEVEL 1 ────────────────────────────────────────────────────────────────

/**
 * Returns a greeting for the given name.
 * If no name is provided, greets "Student" by default.
 */
fun greet(name: String = "Student"): String {
    return name
}

/**
 * Prints information about a person.
 * @param name  Person's name (required)
 * @param age   Person's age (default: 18)
 * @param city  Person's city (default: "Paris")
 */
fun printInfo(name: String, age: Int = 18, city: String = "Paris") {
    println("$name is $age years old and lives in $city.")
}

/**
 * Returns the sum of two integers.
 */
fun add(a: Int, b: Int): Int {
    return a + b
}

/**
 * Returns true if the given number is even, false otherwise.
 */
fun isEven(number: Int): Boolean {
    return number % 2 == 0
}

/**
 * Computes the area of a circle using the formula: π × r².
 */
fun areaOfCircle(radius: Double): Double {
    return PI * radius * radius
}

// ─── LEVEL 2 ────────────────────────────────────────────────────────────────

/**
 * Returns the letter grade corresponding to a numeric score.
 * - 90+  → "A"
 * - 80+  → "B"
 * - 70+  → "C"
 * - 60+  → "D"
 * - < 60 → "F"
 */
fun grade(score: Int): String {
    return when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else        -> "F"
    }
}

/**
 * Returns the largest of three integers.
 */
fun maxOfThree(a: Int, b: Int, c: Int): Int {
    return maxOf(a, b, c)
}

/**
 * Converts a Celsius temperature to Fahrenheit.
 * Formula: (C × 9/5) + 32
 */
fun toFahrenheit(celsius: Double): Double {
    return celsius * 9.0 / 5.0 + 32.0
}

// ─── LEVEL 3 ────────────────────────────────────────────────────────────────

/**
 * Applies a percentage discount to a price.
 * @param price    Original price
 * @param discount Discount fraction (default: 0.1 = 10%)
 * @return         Discounted price
 */
fun applyDiscount(price: Double, discount: Double = 0.1): Double {
    return price * (1.0 - discount)
}

// ─── LEVEL 4 ────────────────────────────────────────────────────────────────

/**
 * Capitalizes the first letter of every word in a sentence.
 */
fun capitalizeWords(sentence: String): String {
    return sentence.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercaseChar() }
    }
}

/**
 * Computes the Body Mass Index (BMI).
 * Formula: weight (kg) / height² (m²)
 */
fun bmi(weight: Double, height: Double): Double {
    return weight / (height * height)
}

/**
 * Evaluates password strength.
 * A strong password must:
 *  - Be at least 8 characters long
 *  - Contain at least one uppercase letter
 *  - Contain at least one lowercase letter
 *  - Contain at least one digit
 *
 * @return true if the password meets all criteria, false otherwise.
 */
fun passwordStrength(password: String): Boolean {
    if (password.length < 8) return false
    if (!password.any { it.isUpperCase() }) return false
    if (!password.any { it.isLowerCase() }) return false
    if (!password.any { it.isDigit() }) return false
    return true
}

/**
 * Filters a list and returns only even numbers.
 */
fun filterEvenNumbers(numbers: List<Int>): List<Int> {
    return numbers.filter { it % 2 == 0 }
}

// ─── LEVEL 5 ────────────────────────────────────────────────────────────────

/**
 * Computes n! (n factorial) recursively.
 * factorial(0) = 1
 * factorial(n) = n × factorial(n - 1)
 */
fun factorial(n: Int): Int {
    return if (n <= 0) 1 else n * factorial(n - 1)
}

/**
 * Returns the nth Fibonacci number using recursion.
 * fibonacci(0) = 0, fibonacci(1) = 1
 */
fun fibonacci(n: Int): Int {
    return when (n) {
        0    -> 0
        1    -> 1
        else -> fibonacci(n - 1) + fibonacci(n - 2)
    }
}

// ─── LEVEL 6 ────────────────────────────────────────────────────────────────

/**
 * Simple interactive calculator.
 * Reads two numbers and an operator (+, -, *, /) from the user
 * and prints the result.
 */
fun miniCalculator() {
    println("Enter first number:")
    val a = readln().toDouble()

    println("Enter second number:")
    val b = readln().toDouble()

    println("Enter operator (+, -, *, /):")
    val operator = readln().trim()

    val result: String = when (operator) {
        "+" -> "${a + b}"
        "-" -> "${a - b}"
        "*" -> "${a * b}"
        "/" -> if (b != 0.0) "${a / b}" else "Error: Division by zero"
        else -> "Error: Unknown operator '$operator'"
    }

    println("Result: $a $operator $b = $result")
}

// ─── LEVEL 7 ────────────────────────────────────────────────────────────────

/**
 * Analyzes a text string and returns a map of statistics:
 *  - "charCount"         → total number of characters (spaces included)
 *  - "wordCount"         → number of words
 *  - "longestWord"       → the longest word in the text
 *  - "averageWordLength" → average word length as a Double
 */
fun analyzeText(text: String): Map<String, Any> {
    val words = text.split(" ").filter { it.isNotEmpty() }
    val charCount = text.length
    val wordCount = words.size
    val longestWord = words.maxByOrNull { it.length } ?: ""
    val averageWordLength = if (words.isEmpty()) 0.0
                            else words.sumOf { it.length }.toDouble() / words.size

    return mapOf(
        "charCount"         to charCount,
        "wordCount"         to wordCount,
        "longestWord"       to longestWord,
        "averageWordLength" to averageWordLength
    )
}


// ─── TEST RUNNER ─────────────────────────────────────────────────────────────

fun main() {
    println("🔍 Running Kotlin Functions Playground Tests...\n")

    var passed = 0
    var failed = 0

    fun verify(name: String, block: () -> Boolean) {
        try {
            check(block()) { "❌ Test failed: $name" }
            println("✅ $name")
            passed++
        } catch (e: Throwable) {
            println("❌ $name → ${e.message}")
            failed++
        }
    }

    // 🟢 LEVEL 1
    verify(name = "greet() with default") { greet() == "Student" }
    verify(name = "greet(\"Alice\")") { greet("Alice") == "Alice" }
    verify("printInfo with all defaults") {
        printInfo("Bob")
        true // Just checking it runs without error
    }
    verify("add(3,5) == 8") { add(3, 5) == 8 }
    verify("isEven(4) == true") { isEven(4) }
    verify("isEven(7) == false") { !isEven(7) }
    verify("areaOfCircle(2.0) ≈ 12.57") {
        val result = areaOfCircle(2.0)
        result in 12.56..12.58
    }

    // 🟡 LEVEL 2
    verify("grade(95) == 'A'") { grade(95) == "A" }
    verify("grade(82) == 'B'") { grade(82) == "B" }
    verify("maxOfThree(3,9,6) == 9") { maxOfThree(3, 9, 6) == 9 }
    verify("toFahrenheit(20.0) == 68.0") { (toFahrenheit(20.0) - 68.0).absoluteValue < 0.1 }

    // 🟠 LEVEL 3
    verify("applyDiscount(100.0) == 90.0") { (applyDiscount(100.0) - 90.0).absoluteValue < 0.001 }
    verify("applyDiscount(100.0, 0.2) == 80.0") { (applyDiscount(100.0, 0.2) - 80.0).absoluteValue < 0.001 }

    // 🟣 LEVEL 4
    verify("capitalizeWords works") { capitalizeWords("hello kotlin world") == "Hello Kotlin World" }
    verify("bmi(70,1.75) ≈ 22.86") { bmi(70.0, 1.75) in 22.8..22.9 }
    verify("passwordStrength detects strong") { passwordStrength("MyPass123") }
    verify("passwordStrength detects weak") { !passwordStrength("weak") }
    verify("filterEvenNumbers works") {
        filterEvenNumbers(listOf(1, 2, 3, 4, 5, 6)) == listOf(2, 4, 6)
    }

    // ⚫ LEVEL 5
    verify("factorial(5) == 120") { factorial(5) == 120 }
    verify("fibonacci(6) == 8") { fibonacci(6) == 8 }

    // 🧠 LEVEL 7
    verify("analyzeText stats") {
        val result = analyzeText("Kotlin is fun and powerful")
        result["charCount"] == 26 &&
                result["wordCount"] == 5 &&
                result["longestWord"] == "powerful" &&
                (result["averageWordLength"] as Double) in 4.0..5.0
    }

    println("\n🎯 TEST SUMMARY: $passed passed, $failed failed.")
    if (failed == 0) println("🎉 All tests passed! Great job!")
    else println("⚠️  Some tests failed. Keep debugging!")
}

// Simple helper for double comparison
private val Double.absoluteValue get() = if (this < 0) -this else this
