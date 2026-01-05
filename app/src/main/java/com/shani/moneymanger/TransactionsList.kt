package com.shani.moneymanger

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionsList(
    transactions: List<Transaction>,  // ⭐ MUST HAVE THIS PARAMETER
    onDeleteTransaction: (Long) -> Unit,
    onEditTransaction: (Transaction) -> Unit
) {
    // ⭐ DO NOT access repository here!
    // ⭐ DO NOT use collectAsState() here!

    Column {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (transactions.isEmpty()) {
            Text(
                text = "No transactions for this period",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            transactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onDelete = onDeleteTransaction,
                    onEdit = onEditTransaction
                )
            }
        }
    }
}