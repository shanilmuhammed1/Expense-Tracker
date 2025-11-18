package com.shani.moneymanger

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String,
    val category: String,
    val description: String,
    val date: String
)

// Add this function after your TransactionEntity data class
fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = this.id,
        amount = this.amount,
        type = TransactionType.valueOf(this.type),
        category = Category.valueOf(this.category),
        description = this.description,
        date = java.time.LocalDateTime.parse(this.date)
    )
}