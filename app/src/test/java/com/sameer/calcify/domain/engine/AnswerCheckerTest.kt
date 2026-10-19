package com.sameer.calcify.domain.engine

import com.sameer.calcify.domain.model.ArithmeticOperation
import com.sameer.calcify.domain.model.ArithmeticQuestion
import java.math.BigDecimal
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerCheckerTest {
    private val integerQuestion = ArithmeticQuestion(
        first = 7,
        second = 5,
        operation = ArithmeticOperation.ADD,
        answer = BigDecimal("12")
    )

    private val roundedDivisionQuestion = ArithmeticQuestion(
        first = 85,
        second = 3,
        operation = ArithmeticOperation.DIVIDE,
        answer = BigDecimal("28.3333333333"),
        acceptedDecimalPlaces = 2
    )

    @Test
    fun integerAnswersIgnoreInsignificantTrailingZeros() {
        assertTrue(AnswerChecker.isCorrect("12.0", integerQuestion))
        assertTrue(AnswerChecker.isCorrect(" 12 ", integerQuestion))
        assertFalse(AnswerChecker.isCorrect("12.1", integerQuestion))
    }

    @Test
    fun divisionAcceptsTheCorrectlyRoundedAnswer() {
        assertTrue(AnswerChecker.isCorrect("28.33", roundedDivisionQuestion))
        assertTrue(AnswerChecker.isCorrect("28.330", roundedDivisionQuestion))
        assertFalse(AnswerChecker.isCorrect("28.27", roundedDivisionQuestion))
        assertFalse(AnswerChecker.isCorrect("28.3", roundedDivisionQuestion))
    }

    @Test
    fun malformedAndBlankAnswersAreRejected() {
        assertFalse(AnswerChecker.isCorrect("", integerQuestion))
        assertFalse(AnswerChecker.isCorrect("twelve", integerQuestion))
    }
}
