package com.sameer.calcify.domain.scoring

enum class AnswerOutcome {
    CORRECT,
    INCORRECT,
    SKIPPED
}

object ScoreCalculator {
    fun pointsFor(outcome: AnswerOutcome): Int = when (outcome) {
        AnswerOutcome.CORRECT -> 10
        AnswerOutcome.INCORRECT -> -2
        AnswerOutcome.SKIPPED -> 0
    }

    fun sessionScore(outcomes: Iterable<AnswerOutcome>): Int =
        outcomes.sumOf(::pointsFor)

    /**
     * Accuracy is the percentage of correct answers among answered questions.
     * Skipped questions are excluded. Returns 0 when nothing was answered.
     */
    fun accuracyPercent(outcomes: Iterable<AnswerOutcome>): Double {
        val answered = outcomes.filter { it != AnswerOutcome.SKIPPED }
        if (answered.isEmpty()) return 0.0
        val correct = answered.count { it == AnswerOutcome.CORRECT }
        return correct * 100.0 / answered.size
    }
}
