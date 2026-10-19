package com.sameer.calcify.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

enum class ArithmeticOperation(val symbol: String) {
    ADD("+"),
    SUBTRACT("−"),
    MULTIPLY("×"),
    DIVIDE("÷")
}

data class ArithmeticQuestion(
    val first: Int,
    val second: Int,
    val operation: ArithmeticOperation,
    val answer: BigDecimal,
    val acceptedDecimalPlaces: Int = 0
) {
    val expression: String
        get() = "$first ${operation.symbol} $second"

    val formattedAnswer: String
        get() = answer.setScale(acceptedDecimalPlaces, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
}
