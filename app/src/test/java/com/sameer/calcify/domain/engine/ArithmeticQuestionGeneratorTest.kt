package com.sameer.calcify.domain.engine

import com.sameer.calcify.domain.model.ArithmeticOperation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ArithmeticQuestionGeneratorTest {
    @Test
    fun generatedQuestionsHaveConsistentAnswers() {
        val generator = ArithmeticQuestionGenerator(Random(42))

        repeat(500) {
            val question = generator.generate(1, 20)
            when (question.operation) {
                ArithmeticOperation.ADD -> assertEquals(question.first + question.second, question.answer.toInt())
                ArithmeticOperation.SUBTRACT -> assertEquals(question.first - question.second, question.answer.toInt())
                ArithmeticOperation.MULTIPLY -> assertEquals(question.first * question.second, question.answer.toInt())
                ArithmeticOperation.DIVIDE -> {
                    assertTrue(question.second > 0)
                    assertEquals(0, question.first % question.second)
                    assertEquals(question.first / question.second, question.answer.toInt())
                }
            }
        }
    }

    @Test
    fun divisionMasteryGeneratesNonZeroDivisorsAndTwoDecimalAnswers() {
        val generator = ArithmeticQuestionGenerator(Random(7))

        repeat(200) {
            val question = generator.generateDivisionQuestion()
            assertEquals(ArithmeticOperation.DIVIDE, question.operation)
            assertTrue(question.second in 2..9)
            assertEquals(2, question.acceptedDecimalPlaces)
            assertTrue(question.first in 10..99)
        }
    }
}
