package com.shani.moneymanger

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen() {
    val context = LocalContext.current
    val repository = TransactionRepository.getInstance(LocalContext.current)
    val scope = rememberCoroutineScope()
    // ⭐ ADD: Manage date range in ChartScreen too
    var currentRange by remember { mutableStateOf(DateRange.current(ViewMode.MONTH)) }

    // ⭐ ADD: Get and filter transactions
    val allTransactions by repository.transactions.collectAsState()

    val filteredTransactions = remember(allTransactions, currentRange) {
        allTransactions.filterByDateRange(currentRange)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (filteredTransactions.isEmpty()) {
                    Toast.makeText(
                        context,
                        "No transactions in ${currentRange.displayText}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    scope.launch {
                        pdfExporter.exportTransactions(
                            context = context,
                            transactions = filteredTransactions,
                            dateRange = currentRange
                        )
                    }
                }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = "Download PDF"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(38.dp))
        }
    }
}