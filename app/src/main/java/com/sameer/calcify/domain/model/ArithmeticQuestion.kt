package com.sameer.calcify.domain.model

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
    val answer: Int
) {
    val expression: String
        get() = "$first ${operation.symbol} $second"
}
