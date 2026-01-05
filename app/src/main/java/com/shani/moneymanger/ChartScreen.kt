package com.shani.moneymanger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    onNavigateBack: () -> Unit
) {
    val repository = TransactionRepository.getInstance(LocalContext.current)

    // ⭐ ADD: Manage date range in ChartScreen too
    var currentRange by remember { mutableStateOf(DateRange.current(ViewMode.MONTH)) }

    // ⭐ ADD: Get and filter transactions
    val allTransactions by repository.transactions.collectAsState()

    val filteredTransactions = remember(allTransactions, currentRange) {
        allTransactions.filterByDateRange(currentRange)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Trend") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Expense Analysis",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ⭐ ADD: Period selector in ChartScreen
            PeriodSelector(
                currentRange = currentRange,
                onRangeChanged = { newRange ->
                    currentRange = newRange
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ⭐ UPDATED: Pass filtered transactions
            ExpenseChart(transactions = filteredTransactions)
        }
    }
}