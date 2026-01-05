package com.shani.moneymanger

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Homescreen(
    onNavigateToChart: () -> Unit
){
    val repository = TransactionRepository.getInstance(LocalContext.current)
    var currentRange by remember { mutableStateOf(DateRange.current(ViewMode.MONTH)) }  // ⭐ FIXED
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null)}


    val allTransactions by repository.transactions.collectAsState()


    val filteredTransactions = remember(allTransactions, currentRange) {
        val filtered = allTransactions.filterByDateRange(currentRange)

        // 🔍 DETAILED DEBUG
        println("════════════════════════════════════")
        println("📅 Period: ${currentRange.displayText}")
        println("📅 Start: ${currentRange.startDate}")
        println("📅 End: ${currentRange.endDate}")
        println("📊 All transactions: ${allTransactions.size}")

        // Print each transaction date
        allTransactions.forEachIndexed { index, transaction ->
            println("Transaction $index: ${transaction.description} - ${transaction.date}")
        }

        println("✅ Filtered transactions: ${filtered.size}")

        // Print filtered transactions
        filtered.forEach { transaction ->
            println("  ✓ ${transaction.description} - ${transaction.date}")
        }

        println("════════════════════════════════════")

        filtered
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "My Expense Tracker",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            PeriodSelector(
                currentRange = currentRange,
                onRangeChanged = { newRange ->
                    currentRange = newRange
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            FinancialSummaryCard(   transactions = filteredTransactions)  // ⭐ FIXED

            Spacer(modifier = Modifier.height(16.dp))

            TransactionsList(
                transactions = filteredTransactions,  // ⭐ FIXED - Added this line
                onDeleteTransaction = { transactionId ->
                    repository.deleteTransaction(transactionId)
                },
                onEditTransaction = { transaction ->
                    selectedTransaction = transaction
                    showEditDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToChart,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text("View Expense Trend")  // ⭐ Fixed typo
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    // Add Transaction Dialog
    if (showAddDialog) {
        AddTransactionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { transaction ->
                repository.addTransaction(transaction)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedTransaction != null){
        EditTransactionDialog(
            transaction = selectedTransaction!!,
            onDismiss = {
                showEditDialog = false
                selectedTransaction = null  // ⭐ ADD THIS - Clear after closing
            },
            onConfirm = { updatedTransaction ->
                repository.updateTransaction(updatedTransaction)
                showEditDialog = false
                selectedTransaction = null  // ⭐ ADD THIS - Clear after updating
            }
        )
    }
}