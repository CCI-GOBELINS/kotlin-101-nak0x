package com.android.one

// ─────────────────────────────────────────────────────────────────────────────
// Lesson 2b — Kotlin List Processing
// Follow the instructions in README_02.md
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Helper — runs a named test block and records pass/fail.
 * Re-uses the same style as Lesson2.kt so the output is consistent.
 */
fun runtest(name: String, block: () -> Boolean, passed: MutableList<String>, failed: MutableList<String>) {
    try {
        if (block()) passed.add(name)
        else failed.add("$name → returned false")
    } catch (e: Throwable) {
        failed.add("$name → ${e.message}")
    }
}

// ─── Exercise 1 — Immutable List ─────────────────────────────────────────────

/**
 * Creates and returns an immutable list of 5 integers.
 * Uses `listOf` which produces a read-only List<Int>.
 */
fun ex1CreateImmutableList(): List<Int> {
    return listOf(1, 2, 3, 4, 5)
}

// ─── Exercise 2 — Mutable List ───────────────────────────────────────────────

/**
 * Creates a mutable list of 3 strings, adds one element, and returns it.
 * Uses `mutableListOf` which allows add/remove operations.
 */
fun ex2CreateMutableList(): MutableList<String> {
    val list = mutableListOf("Kotlin", "Java", "Python")
    list.add("Swift")   // adding a fourth language
    return list
}

// ─── Exercise 3 — Filter Even Numbers ────────────────────────────────────────

/**
 * Creates a list from 1 to 10, filters even numbers, and returns the result.
 */
fun ex3FilterEvenNumbers(): List<Int> {
    val numbers = (1..10).toList()
    return numbers.filter { it % 2 == 0 }
}

// ─── Exercise 4 — Filter and Map Ages ────────────────────────────────────────

/**
 * Given a hardcoded list of ages:
 *  1. Keeps only ages >= 18 (adults)
 *  2. Maps each age to a string like "Adult: 25"
 *  3. Returns the resulting list
 */
fun ex4FilterAndMapAges(): List<String> {
    val ages = listOf(12, 17, 25, 30, 16, 45, 18, 8)
    return ages
        .filter { it >= 18 }
        .map { "Adult: $it" }
}

// ─── Exercise 5 — Flatten Nested Lists ───────────────────────────────────────

/**
 * Creates a nested list `[[1, 2], [3, 4], [5]]` and returns it flattened.
 * Uses the `flatten()` extension function.
 */
fun ex5FlattenList(): List<Int> {
    val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
    return nested.flatten()
}

// ─── Exercise 6 — FlatMap Words ──────────────────────────────────────────────

/**
 * Creates a list of phrases, then uses `flatMap` with `split(" ")` to extract
 * every individual word and returns them as a flat list.
 */
fun ex6FlatMapWords(): List<String> {
    val phrases = listOf("Kotlin is fun", "I love lists")
    return phrases.flatMap { it.split(" ") }
}

// ─── Exercise 7 — Eager Processing ───────────────────────────────────────────

/**
 * Eager evaluation example on a large range:
 *  1. Creates a list from 1 to 1_000_000
 *  2. Filters numbers divisible by 3
 *  3. Maps them to their squares
 *  4. Returns the first 5 results
 *  5. Prints the execution time
 *
 *  With eager evaluation every intermediate list is fully materialized in memory
 *  before the next step, so it processes all one million elements.
 */
fun ex7EagerProcessing(): List<Int> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000).toList()
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)

    val end = System.currentTimeMillis()
    println("[Eager]  Time: ${end - start} ms  →  $result")

    return result
}

// ─── Exercise 8 — Lazy Processing ────────────────────────────────────────────

/**
 * Lazy evaluation example using `.asSequence()`:
 *  1. Same operations as Exercise 7
 *  2. Returns the first 5 results
 *  3. Prints the execution time
 *
 *  With lazy evaluation, elements are processed one-by-one and the chain stops
 *  as soon as 5 results are found — far fewer than a million elements are touched.
 */
fun ex8LazyProcessing(): List<Int> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000).asSequence()
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)
        .toList()

    val end = System.currentTimeMillis()
    println("[Lazy]   Time: ${end - start} ms  →  $result")

    return result
}

// ─── Exercise 9 — Chain Multiple Operations ───────────────────────────────────

/**
 * Given a list of names:
 *  1. Filters those starting with 'A'
 *  2. Converts them to uppercase
 *  3. Sorts alphabetically
 *  4. Returns the result
 */
fun ex9FilterAndSortNames(): List<String> {
    val names = listOf("Alice", "Bob", "Anna", "Charlie", "Aaron", "David", "Amelia")
    return names
        .filter { it.startsWith('A') }
        .map { it.uppercase() }
        .sorted()
}

// ─── Main / Test Runner ───────────────────────────────────────────────────────

fun main() {
    println("📋 Running Lesson 2b — List Processing Tests...\n")

    val passed = mutableListOf<String>()
    val failed  = mutableListOf<String>()

    // Exercise 1
    runtest("ex1 — immutable list of 5 ints", {
        val list = ex1CreateImmutableList()
        list.size == 5
    }, passed, failed)

    // Exercise 2
    runtest("ex2 — mutable list with 4 elements", {
        val list = ex2CreateMutableList()
        list.size == 4
    }, passed, failed)

    // Exercise 3
    runtest("ex3 — even numbers from 1..10", {
        ex3FilterEvenNumbers() == listOf(2, 4, 6, 8, 10)
    }, passed, failed)

    // Exercise 4
    runtest("ex4 — filter and map ages", {
        val result = ex4FilterAndMapAges()
        result.all { it.startsWith("Adult:") } &&
                result.none { it.contains("12") || it.contains("17") || it.contains("16") || it.contains("8") }
    }, passed, failed)

    // Exercise 5
    runtest("ex5 — flatten nested list", {
        ex5FlattenList() == listOf(1, 2, 3, 4, 5)
    }, passed, failed)

    // Exercise 6
    runtest("ex6 — flatMap words", {
        val result = ex6FlatMapWords()
        result == listOf("Kotlin", "is", "fun", "I", "love", "lists")
    }, passed, failed)

    // Exercise 7
    runtest("ex7 — eager processing (first 5 squares of multiples of 3)", {
        val result = ex7EagerProcessing()
        result == listOf(9, 36, 81, 144, 225)   // 3²,6²,9²,12²,15²
    }, passed, failed)

    // Exercise 8
    runtest("ex8 — lazy processing (same result as ex7)", {
        val result = ex8LazyProcessing()
        result == listOf(9, 36, 81, 144, 225)
    }, passed, failed)

    // Exercise 9
    runtest("ex9 — filter, uppercase, sort names starting with A", {
        ex9FilterAndSortNames() == listOf("AARON", "ALICE", "AMELIA", "ANNA")
    }, passed, failed)

    println("\n🎯 TEST SUMMARY: ${passed.size} passed, ${failed.size} failed.")
    if (passed.size > 0) passed.forEach { println("  ✅ $it") }
    if (failed.size > 0) failed.forEach { println("  ❌ $it") }
    if (failed.isEmpty()) println("\n🎉 All tests passed! Great job!")
    else println("\n⚠️  Some tests failed. Keep debugging!")
}