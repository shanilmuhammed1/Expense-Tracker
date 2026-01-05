package com.shani.moneymanger

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FinancialSummaryCard(
    transactions: List<Transaction>
) {
    val summary = remember(transactions) {
        val totalIncome = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryItem(
                label = "Income",
                amount = summary.totalIncome,
                color = Color(0xFF4CAF50)
            )
            SummaryItem(
                label = "Expense",
                amount = summary.totalExpense,
                color = Color(0xFFFF5722)
            )
            SummaryItem(
                label = "Savings",
                amount = summary.Savings,
                color = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    amount: Double,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "₹${"%.2f".format(amount)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}