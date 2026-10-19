package com.sameer.calcify.domain.engine

import com.sameer.calcify.domain.model.ArithmeticOperation
import com.sameer.calcify.domain.model.ArithmeticQuestion
import kotlin.random.Random

/**
 * Generates basic integer questions. Kept free of Android dependencies so it can be unit-tested.
 */
class ArithmeticQuestionGenerator(
    private val random: Random = Random.Default
) {
    fun generate(min: Int = 1, max: Int = 20): ArithmeticQuestion {
        require(min <= max) { "min must be less than or equal to max" }

        val operation = ArithmeticOperation.entries.random(random)
        var first = random.nextInt(min, max + 1)
        var second = random.nextInt(min, max + 1)

        if (operation == ArithmeticOperation.DIVIDE) {
            // Generate exact integer division and never divide by zero.
            second = random.nextInt(min.coerceAtLeast(1), max.coerceAtLeast(1) + 1)
            val quotient = random.nextInt(min.coerceAtLeast(1), max.coerceAtLeast(1) + 1)
            first = second * quotient
            return ArithmeticQuestion(first, second, operation, quotient)
        }

        if (operation == ArithmeticOperation.SUBTRACT && second > first) {
            val temporary = first
            first = second
            second = temporary
        }

        val answer = when (operation) {
            ArithmeticOperation.ADD -> first + second
            ArithmeticOperation.SUBTRACT -> first - second
            ArithmeticOperation.MULTIPLY -> first * second
            ArithmeticOperation.DIVIDE -> error("Division handled above")
        }

        return ArithmeticQuestion(first, second, operation, answer)
    }
}
