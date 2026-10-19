package com.sameer.calcify.domain.engine

import com.sameer.calcify.domain.model.ArithmeticOperation
import com.sameer.calcify.domain.model.ArithmeticQuestion
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

/**
 * Generates basic integer questions and decimal-division practice.
 * This class has no Android dependencies so it can be unit-tested.
 */
class ArithmeticQuestionGenerator(
    private val random: Random = Random.Default
) {
    fun generate(min: Int = 1, max: Int = 20): ArithmeticQuestion {
        require(min <= max) { "min must be less than or equal to max" }
        require(min >= 0) { "min must be non-negative" }
        require(max >= 1) { "max must be at least 1" }

        val operation = ArithmeticOperation.entries.random(random)
        var first = random.nextInt(min, max + 1)
        var second = random.nextInt(min, max + 1)

        if (operation == ArithmeticOperation.DIVIDE) {
            second = random.nextInt(1, max + 1)
            val quotient = random.nextInt(1, max + 1)
            first = second * quotient
            return ArithmeticQuestion(
                first = first,
                second = second,
                operation = operation,
                answer = BigDecimal.valueOf(quotient.toLong())
            )
        }

        if (operation == ArithmeticOperation.SUBTRACT && second > first) {
            val temporary = first
            first = second
            second = temporary
        }

        val answer = when (operation) {
            ArithmeticOperation.ADD -> BigDecimal.valueOf((first + second).toLong())
            ArithmeticOperation.SUBTRACT -> BigDecimal.valueOf((first - second).toLong())
            ArithmeticOperation.MULTIPLY -> BigDecimal.valueOf(first.toLong() * second.toLong())
            ArithmeticOperation.DIVIDE -> error("Division handled above")
        }

        return ArithmeticQuestion(first, second, operation, answer)
    }

    /**
     * Creates division questions whose correct response is rounded to two decimal places.
     * The exact quotient is retained internally and rounded only for answer checking/display.
     */
    fun generateDivisionQuestion(): ArithmeticQuestion {
        val divisor = random.nextInt(2, 10)
        val dividend = random.nextInt(10, 100)
        val exactAnswer = BigDecimal.valueOf(dividend.toLong())
            .divide(BigDecimal.valueOf(divisor.toLong()), 10, RoundingMode.HALF_UP)

        return ArithmeticQuestion(
            first = dividend,
            second = divisor,
            operation = ArithmeticOperation.DIVIDE,
            answer = exactAnswer,
            acceptedDecimalPlaces = 2
        )
    }
}
