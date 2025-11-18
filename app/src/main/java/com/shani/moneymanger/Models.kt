package com.shani.moneymanger

import java.time.LocalDateTime

enum class TimePeriod(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

enum class TransactionType(val displayName: String) {
    INCOME("Income"),
    EXPENSE("Expense")
}

enum class Category(val displayName: String){
    SALARY("Salary"),
    BUSINESS("Business"),
    INVESTMENT("Investment"),
    GIFTS("Gifts"),
    RENT("Rent"),
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    HEALTH("Health"),
    OTHER("Other")
}

data class Transaction(
    val id: Long= 0,
    val amount: Double,
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category,
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.now()
)

data class FinancialSummary(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
){
    val Savings: Double
        get() = totalIncome - totalExpense
}