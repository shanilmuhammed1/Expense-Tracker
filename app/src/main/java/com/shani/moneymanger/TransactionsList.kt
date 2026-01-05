package com.shani.moneymanger

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionsList(
    transactions: List<Transaction>,
    onDeleteTransaction: (Long) -> Unit,
    onEditTransaction: (Transaction) -> Unit
) {
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = transactions,
                    key = { transaction -> transaction.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onDelete = onDeleteTransaction,
                        onEdit = onEditTransaction
                    )
                }
            }
        }
    }
}