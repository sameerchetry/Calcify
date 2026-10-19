package com.sameer.calcify.domain.engine

import com.sameer.calcify.domain.model.ArithmeticQuestion
import java.math.RoundingMode

/**
 * Checks numeric answers without relying on binary floating-point equality.
 * Division questions are accepted at the decimal precision declared by the question.
 */
object AnswerChecker {
    fun isCorrect(input: String, question: ArithmeticQuestion): Boolean {
        val parsed = input.trim().toBigDecimalOrNull() ?: return false
        val expected = question.answer.setScale(
            question.acceptedDecimalPlaces,
            RoundingMode.HALF_UP
        )
        return parsed.compareTo(expected) == 0
    }
}
