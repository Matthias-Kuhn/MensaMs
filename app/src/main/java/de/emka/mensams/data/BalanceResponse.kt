package de.emka.mensams.data

data class BalanceResponse(
    val balance: Int,
    val cardId: String,
    val universityId: String
)