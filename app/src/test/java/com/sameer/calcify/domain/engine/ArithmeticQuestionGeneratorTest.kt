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
                ArithmeticOperation.ADD -> assertEquals(question.first + question.second, question.answer)
                ArithmeticOperation.SUBTRACT -> assertEquals(question.first - question.second, question.answer)
                ArithmeticOperation.MULTIPLY -> assertEquals(question.first * question.second, question.answer)
                ArithmeticOperation.DIVIDE -> {
                    assertTrue(question.second > 0)
                    assertEquals(0, question.first % question.second)
                    assertEquals(question.first / question.second, question.answer)
                }
            }
        }
    }
}
