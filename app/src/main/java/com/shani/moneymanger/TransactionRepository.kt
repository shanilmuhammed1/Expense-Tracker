package com.shani.moneymanger

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class TransactionRepository private constructor(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val transactionDao = database.transactionDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    init {
        scope.launch {
            transactionDao.getAllTransactions().collect { entities ->
                val transactions = entities.map { it.toTransaction() }
                _transactions.value = transactions }
        }
    }


    fun addTransaction(transaction: Transaction) {
        scope.launch { val entity = TransactionEntity( amount = transaction.amount,
            type = transaction.type.name,
            category = transaction.category.name,
            description = transaction.description,
            date = transaction.date.toString())
            transactionDao.insertTransaction(entity)
        }

    }

    fun deleteTransaction(transactionId: Long) {
        scope.launch {
            val transaction = _transactions.value.find { it.id == transactionId }
            transaction?.let {
                val entity = TransactionEntity(
                    id = it.id,
                    amount = it.amount,
                    type = it.type.name,
                    category = it.category.name,
                    description = it.description,
                    date = it.date.toString()
                )
                transactionDao.deleteTransaction(entity)
            }
        }
    }

    fun getFinancialSummary(): FinancialSummary {
        val allTransactions = _transactions.value
        val totalIncome = allTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        val totalExpense = allTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        return FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(context: Context): TransactionRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransactionRepository(context).also { INSTANCE = it }
            }
        }
    }
}