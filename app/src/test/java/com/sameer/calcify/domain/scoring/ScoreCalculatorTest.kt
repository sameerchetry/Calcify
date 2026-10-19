package com.sameer.calcify.domain.scoring

import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreCalculatorTest {
    @Test
    fun correctIncorrectAndSkippedHaveExpectedPoints() {
        assertEquals(10, ScoreCalculator.pointsFor(AnswerOutcome.CORRECT))
        assertEquals(-2, ScoreCalculator.pointsFor(AnswerOutcome.INCORRECT))
        assertEquals(0, ScoreCalculator.pointsFor(AnswerOutcome.SKIPPED))
    }

    @Test
    fun sessionScoreCanBeNegative() {
        assertEquals(-4, ScoreCalculator.sessionScore(
            listOf(AnswerOutcome.INCORRECT, AnswerOutcome.INCORRECT)
        ))
    }

    @Test
    fun skippedAnswersAreExcludedFromAccuracy() {
        val outcomes = listOf(
            AnswerOutcome.CORRECT,
            AnswerOutcome.INCORRECT,
            AnswerOutcome.SKIPPED
        )
        assertEquals(50.0, ScoreCalculator.accuracyPercent(outcomes), 0.001)
    }

    @Test
    fun noAnsweredQuestionsHasZeroAccuracy() {
        assertEquals(0.0, ScoreCalculator.accuracyPercent(listOf(AnswerOutcome.SKIPPED)), 0.001)
    }
}
